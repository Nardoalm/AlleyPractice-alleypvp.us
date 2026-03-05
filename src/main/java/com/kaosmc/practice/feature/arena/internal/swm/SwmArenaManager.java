package com.kaosmc.practice.feature.arena.internal.swm;

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
import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.FileUtil;
import com.kaosmc.practice.common.VoidChunkGenerator;
import com.kaosmc.practice.common.logger.Logger;
import com.kaosmc.practice.core.config.ConfigService;
import com.kaosmc.practice.feature.arena.internal.types.StandAloneArena;
import com.kaosmc.practice.feature.arena.schematic.ArenaSchematicService;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Integra o gerenciamento de cópias de arena com SlimeWorldManager.
 * Quando habilitado, cada cópia temporária de arena standalone roda em um mundo SWM dedicado.
 */
public class SwmArenaManager implements ArenaCopyManager {
    private static final String SWM_PLUGIN_NAME = "SlimeWorldManager";
    private static final String TEMPLATE_PREFIX = "kaos_template_";
    private static final String COPY_PREFIX = "kaos_copy_";

    private final KaosPractice plugin;
    private final ConfigService configService;
    private final ArenaSchematicService arenaSchematicService;
    private final Map<String, SlimeWorld> templateWorldCache = new ConcurrentHashMap<>();

    private boolean enabled;
    private SlimePlugin slimePlugin;
    private SlimeLoader slimeLoader;

    public SwmArenaManager(KaosPractice plugin, ConfigService configService, ArenaSchematicService arenaSchematicService) {
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
                Logger.info("Gerenciamento de arenas via SWM está desativado no settings.yml.");
                return;
            }

            Plugin possiblePlugin = Bukkit.getPluginManager().getPlugin(SWM_PLUGIN_NAME);
            if (!(possiblePlugin instanceof SlimePlugin)) {
                this.enabled = false;
                Logger.error("SlimeWorldManager não encontrado ou incompatível. Retornando para gerenciamento por schematic.");
                return;
            }

            this.slimePlugin = (SlimePlugin) possiblePlugin;
            this.slimeLoader = this.slimePlugin.getLoader(loaderId);
            if (this.slimeLoader == null) {
                this.enabled = false;
                Logger.error("Loader SWM '" + loaderId + "' não encontrado. Retornando para gerenciamento por schematic.");
                return;
            }

            this.enabled = true;
            Logger.info("Gerenciamento de arenas via SWM ativado com loader: " + loaderId);
        } catch (Throwable throwable) {
            this.enabled = false;

            if (throwable instanceof Exception) {
                Logger.logException("Falha ao inicializar integração SWM. Retornando para gerenciamento por schematic.", (Exception) throwable);
            } else {
                System.err.println("ERRO FATAL (NÃO-EXCEÇÃO) NO SWM:");
                throwable.printStackTrace();
            }
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

        // SOLUÇÃO: Usamos UUID curto + Timestamp para garantir que o nome NUNCA se repita no banco de dados
        String uniqueSuffix = java.util.UUID.randomUUID().toString().substring(0, 8);
        String copyWorldName = COPY_PREFIX + arenaKey + "_" + copyId + "_" + uniqueSuffix;

        try {
            SlimeWorld templateWorld = this.getOrCreateTemplateWorld(originalArena, templateName);
            if (templateWorld == null) {
                return null;
            }

            // Verificação extra de segurança
            if (this.slimePlugin.getLoader(this.configService.getSettingsConfig().getString("arena-management.swm.loader")).worldExists(copyWorldName)) {
                // Se por um milagre ainda existir, tentamos gerar outro nome recursivamente ou apenas falhar
                return createTemporaryArenaCopy(originalArena, copyId);
            }

            SlimeWorld copiedWorld = templateWorld.clone(copyWorldName);
            this.slimePlugin.generateWorld(copiedWorld);

            World bukkitWorld = Bukkit.getWorld(copyWorldName);
            if (bukkitWorld == null) {
                Logger.error("Falha ao gerar o mundo SWM '" + copyWorldName + "'.");
                return null;
            }

            Location copyOrigin = getCopyOrigin(originalArena, bukkitWorld);
            StandAloneArena copiedArena = originalArena.createCopy(bukkitWorld, copyOrigin, copyId);
            if (copiedArena.getPos1() != null) {
                copiedArena.setHeightLimit(copiedArena.getPos1().getBlockY() + copiedArena.getHeightLimit());
            }
            copiedArena.setManagedBySlimeWorldManager(true);
            return copiedArena;
        } catch (Exception exception) {
            // Se o erro for "WorldAlreadyExists", agora sabemos que o sufixo resolve
            Logger.logException("Falha ao criar cópia SWM da arena " + originalArena.getName(), exception);
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
        Bukkit.unloadWorld(world, false);

        File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
        if (worldFolder.exists()) {
            FileUtil.deleteWorldFolder(worldFolder);
        }
        return true;
    }

    @Override
    public void shutdown() {
        this.templateWorldCache.clear();
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
        String importWorldName = "swm_import_" + sanitizeName(originalArena.getName()) + "_" + System.currentTimeMillis();
        File worldFolder = new File(Bukkit.getWorldContainer(), importWorldName);

        cleanupWorld(importWorldName, worldFolder);

        WorldCreator creator = new WorldCreator(importWorldName);
        creator.generateStructures(false).generator(new VoidChunkGenerator());
        World tempImportWorld = creator.createWorld();

        if (tempImportWorld == null) {
            Logger.error("Falha ao criar mundo temporário de importação para o template SWM " + templateName);
            return;
        }

        Location importOrigin = getCopyOrigin(originalArena, tempImportWorld);
        File schematicFile = this.arenaSchematicService.getSchematicFile(originalArena);
        this.arenaSchematicService.paste(importOrigin, schematicFile);

        Bukkit.unloadWorld(tempImportWorld, true);

        try {
            this.slimePlugin.importWorld(worldFolder, templateName, this.slimeLoader);
            Logger.info("Template SWM importado: " + templateName);
        } catch (WorldAlreadyExistsException ignored) {
            Logger.info("Template SWM já existe: " + templateName);
        } catch (WorldLoadedException | WorldTooBigException | com.grinderwolf.swm.api.exceptions.InvalidWorldException | IOException exception) {
            Logger.logException("Falha ao importar template SWM: " + templateName, exception);
        } finally {
            if (worldFolder.exists()) {
                FileUtil.deleteWorldFolder(worldFolder);
            }
        }
    }

    private void cleanupWorld(String worldName, File worldFolder) {
        World loadedWorld = Bukkit.getWorld(worldName);
        if (loadedWorld != null) {
            loadedWorld.getPlayers().forEach(player -> player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation()));
            Bukkit.unloadWorld(loadedWorld, false);
        }
        if (worldFolder.exists()) {
            FileUtil.deleteWorldFolder(worldFolder);
        }
    }

    private SlimePropertyMap createTemplatePropertyMap() {
        SlimePropertyMap propertyMap = new SlimePropertyMap();
        propertyMap.setInt(SlimeProperties.SPAWN_X, 0);
        propertyMap.setInt(SlimeProperties.SPAWN_Y, 100);
        propertyMap.setInt(SlimeProperties.SPAWN_Z, 0);
        propertyMap.setBoolean(SlimeProperties.ALLOW_MONSTERS, false);
        propertyMap.setBoolean(SlimeProperties.ALLOW_ANIMALS, false);
        propertyMap.setBoolean(SlimeProperties.PVP, true);
        propertyMap.setString(SlimeProperties.DIFFICULTY, Difficulty.NORMAL.name());
        propertyMap.setString(SlimeProperties.ENVIRONMENT, World.Environment.NORMAL.name());
        propertyMap.setString(SlimeProperties.WORLD_TYPE, WorldType.FLAT.name());
        return propertyMap;
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
}
