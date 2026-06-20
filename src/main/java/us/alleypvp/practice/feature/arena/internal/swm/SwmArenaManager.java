package us.alleypvp.practice.feature.arena.internal.swm;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldAlreadyExistsException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.exceptions.WorldLoadedException;
import com.grinderwolf.swm.api.exceptions.WorldTooBigException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.common.FileUtil;
import us.alleypvp.practice.common.VoidChunkGenerator;
import us.alleypvp.practice.common.logger.Logger;
import us.alleypvp.practice.core.config.ConfigService;
import us.alleypvp.practice.feature.arena.internal.types.StandAloneArena;
import us.alleypvp.practice.feature.arena.schematic.ArenaSchematicService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SwmArenaManager implements ArenaCopyManager {
    private static final String SWM_PLUGIN_NAME = "SlimeWorldManager";
    private static final String TEMPLATE_PREFIX = "kaos_template_";
    private static final String COPY_PREFIX = "kaos_copy_";
    private static final String IMPORT_PREFIX = "swm_import_";
    private static final int IMPORT_CLEANUP_RETRIES = 5;
    private static final long IMPORT_CLEANUP_RETRY_DELAY_TICKS = 20L;

    private final AlleyPractice plugin;
    private final ConfigService configService;
    private final ArenaSchematicService arenaSchematicService;
    private final Map<String, SlimeWorld> templateWorldCache = new ConcurrentHashMap<>();

    private boolean enabled;
    private SlimePlugin slimePlugin;
    private SlimeLoader slimeLoader;

    public SwmArenaManager(AlleyPractice plugin, ConfigService configService, ArenaSchematicService arenaSchematicService) {
        this.plugin = plugin;
        this.configService = configService;
        this.arenaSchematicService = arenaSchematicService;
    }

    @Override
    public void initialize() {
        try {
            FileConfiguration settings = this.configService.getSettingsConfig();
            boolean swmEnabledInConfig = settings.getBoolean("arena-management.swm.enabled", false);
            String loaderId = settings.getString("arena-management.swm.loader", "file");

            if (!swmEnabledInConfig) {
                this.enabled = false;
                return;
            }

            Plugin possiblePlugin = Bukkit.getPluginManager().getPlugin(SWM_PLUGIN_NAME);
            if (!(possiblePlugin instanceof SlimePlugin)) {
                this.enabled = false;
                return;
            }

            this.slimePlugin = (SlimePlugin) possiblePlugin;
            this.slimeLoader = this.slimePlugin.getLoader(loaderId);
            if (this.slimeLoader == null) {
                this.enabled = false;
                return;
            }

            this.enabled = true;
            this.cleanupCorruptedTemplates();
            this.cleanupStaleImportWorlds();
        } catch (Throwable throwable) {
            this.enabled = false;
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public StandAloneArena createTemporaryArenaCopy(StandAloneArena originalArena, int copyId) {
        if (!enabled) {
            return null;
        }

        String arenaKey = sanitizeName(originalArena.getName());
        String templateName = TEMPLATE_PREFIX + arenaKey;

        String uniqueSuffix = java.util.UUID.randomUUID().toString().substring(0, 8);
        String copyWorldName = COPY_PREFIX + arenaKey + "_" + copyId + "_" + uniqueSuffix;

        try {
            SlimeWorld templateWorld = this.getOrCreateTemplateWorld(originalArena, templateName);
            if (templateWorld == null) {
                return null;
            }

            this.ensureSafeProperties(templateWorld);

            if (this.slimeLoader.worldExists(copyWorldName)) {
                this.slimeLoader.deleteWorld(copyWorldName);
            }

            SlimeWorld copiedWorld = templateWorld.clone(copyWorldName, this.slimeLoader);

            this.ensureSafeProperties(copiedWorld);

            this.slimePlugin.generateWorld(copiedWorld);

            World bukkitWorld = Bukkit.getWorld(copyWorldName);
            if (bukkitWorld == null) {
                return null;
            }

            Location copyOrigin = getCopyOrigin(originalArena, bukkitWorld);
            StandAloneArena copiedArena = originalArena.createCopy(bukkitWorld, copyOrigin, copyId);

            if (copiedArena.getPos1() != null) {
                copiedArena.setHeightLimit(copiedArena.getPos1().getBlockY() + copiedArena.getHeightLimit());
                copiedArena.getPos1().getChunk().load(true);
            }

            if (copiedArena.getPos2() != null) {
                copiedArena.getPos2().getChunk().load(true);
            }

            copiedArena.setManagedBySlimeWorldManager(true);
            return copiedArena;

        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    public boolean deleteTemporaryArena(StandAloneArena arena) {
        if (!enabled || !arena.isManagedBySlimeWorldManager()) {
            return false;
        }

        World world = arena.getMinimum() != null ? arena.getMinimum().getWorld() : null;
        if (world == null) {
            return true;
        }

        String worldName = world.getName();

        world.getPlayers().forEach(player -> player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation()));

        System.gc();
        Bukkit.unloadWorld(world, false);

        try {
            if (this.slimeLoader != null && this.slimeLoader.worldExists(worldName)) {
                this.slimeLoader.deleteWorld(worldName);
            }
        } catch (Exception exception) {
        }

        File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
        if (worldFolder.exists()) {
            FileUtil.deleteWorldFolder(worldFolder);
        }
        return true;
    }

    @Override
    public void shutdown() {
        this.templateWorldCache.clear();
        this.cleanupStaleImportWorlds();
    }

    private SlimeWorld getOrCreateTemplateWorld(StandAloneArena originalArena, String templateName) throws IOException, CorruptedWorldException, NewerFormatException, WorldInUseException, UnknownWorldException {
        SlimeWorld cached = this.templateWorldCache.get(templateName);
        if (cached != null) {
            return cached;
        }

        SlimeWorld loaded;
        try {
            loaded = this.slimePlugin.loadWorld(this.slimeLoader, templateName, true, this.createTemplatePropertyMap());
        } catch (UnknownWorldException exception) {
            this.importTemplateWorld(originalArena, templateName);
            loaded = this.slimePlugin.loadWorld(this.slimeLoader, templateName, true, this.createTemplatePropertyMap());
        }

        this.templateWorldCache.put(templateName, loaded);
        return loaded;
    }

    private void importTemplateWorld(StandAloneArena originalArena, String templateName) {
        String importWorldName = IMPORT_PREFIX + sanitizeName(originalArena.getName()) + "_" + System.currentTimeMillis();
        File worldFolder = new File(Bukkit.getWorldContainer(), importWorldName);

        cleanupWorld(importWorldName, worldFolder);

        WorldCreator creator = new WorldCreator(importWorldName);
        creator.generateStructures(false).generator(new VoidChunkGenerator());
        World tempImportWorld = creator.createWorld();

        if (tempImportWorld == null) {
            scheduleCleanupWorld(importWorldName, worldFolder, IMPORT_CLEANUP_RETRIES);
            return;
        }

        Location importOrigin = getCopyOrigin(originalArena, tempImportWorld);
        File schematicFile = this.arenaSchematicService.getSchematicFile(originalArena);
        this.arenaSchematicService.paste(importOrigin, schematicFile);

        System.gc();
        unloadWorld(importWorldName, true);

        try {
            this.slimePlugin.importWorld(worldFolder, templateName, this.slimeLoader);
        } catch (Throwable throwable) {
            File swmFolder = new File(this.plugin.getDataFolder(), "storage/swm_import_" + sanitizeName(originalArena.getName()));
            if (swmFolder.exists()) {
                FileUtil.deleteWorldFolder(swmFolder);
            }
            throw new RuntimeException("SWM template data corrupted or invalid for " + originalArena.getName(), throwable);
        } finally {
            scheduleCleanupWorld(importWorldName, worldFolder, IMPORT_CLEANUP_RETRIES);
        }
    }

    private void cleanupWorld(String worldName, File worldFolder) {
        unloadWorld(worldName, false);
        if (worldFolder.exists()) {
            System.gc();
            FileUtil.deleteWorldFolder(worldFolder);
        }
    }

    private void unloadWorld(String worldName, boolean save) {
        World loadedWorld = Bukkit.getWorld(worldName);
        if (loadedWorld == null) {
            return;
        }

        loadedWorld.getPlayers().forEach(player -> player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation()));
        System.gc();
        Bukkit.unloadWorld(loadedWorld, save);
    }

    private void scheduleCleanupWorld(String worldName, File worldFolder, int remainingAttempts) {
        cleanupWorld(worldName, worldFolder);
        if (!worldFolder.exists() || remainingAttempts <= 0) {
            return;
        }

        if (!this.plugin.isEnabled()) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(this.plugin, () ->
                scheduleCleanupWorld(worldName, worldFolder, remainingAttempts - 1), IMPORT_CLEANUP_RETRY_DELAY_TICKS);
    }

    private SlimePropertyMap createTemplatePropertyMap() {
        SlimePropertyMap propertyMap = new SlimePropertyMap();
        propertyMap.setInt(SlimeProperties.SPAWN_X, 0);
        propertyMap.setInt(SlimeProperties.SPAWN_Y, 100);
        propertyMap.setInt(SlimeProperties.SPAWN_Z, 0);
        return propertyMap;
    }

    private void ensureSafeProperties(SlimeWorld world) {
        if (world == null || world.getPropertyMap() == null) {
            return;
        }
    }

    private Location getCopyOrigin(StandAloneArena originalArena, World world) {
        Location location = new Location(world, 0, 100, 0);

        Location originalPos1 = originalArena.getPos1();
        Location originalMin = originalArena.getMinimum();
        Location originalMax = originalArena.getMaximum();

        if (originalPos1 != null && originalMin != null && originalMax != null) {
            int actualMinY = Math.min(originalMin.getBlockY(), originalMax.getBlockY());
            int pos1OffsetFromActualMin = originalPos1.getBlockY() - actualMinY;
            int targetMinY = 100 - pos1OffsetFromActualMin;
            location.setY(targetMinY);
        }

        return location;
    }

    private String sanitizeName(String input) {
        return input.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_]", "_");
    }

    private void cleanupCorruptedTemplates() {
        try {
            for (String worldName : this.slimeLoader.listWorlds()) {
                if (worldName.startsWith(TEMPLATE_PREFIX)) {
                    this.slimeLoader.deleteWorld(worldName);
                }
            }
            this.templateWorldCache.clear();
        } catch (Exception e) {
        }
    }

    private void cleanupStaleImportWorlds() {
        File worldContainer = Bukkit.getWorldContainer();
        File[] staleFolders = worldContainer.listFiles(file ->
                file.isDirectory() && file.getName().startsWith(IMPORT_PREFIX));

        if (staleFolders == null || staleFolders.length == 0) {
            return;
        }

        for (File staleFolder : staleFolders) {
            scheduleCleanupWorld(staleFolder.getName(), staleFolder, IMPORT_CLEANUP_RETRIES);
        }
    }
}