package us.alleypvp.practice.feature.arena.internal;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.bootstrap.KaosContext;
import us.alleypvp.practice.bootstrap.annotation.Service;
import us.alleypvp.practice.common.FileUtil;
import us.alleypvp.practice.common.VoidChunkGenerator;
import us.alleypvp.practice.common.logger.Logger;
import us.alleypvp.practice.common.serializer.Serializer;
import us.alleypvp.practice.core.config.ConfigService;
import us.alleypvp.practice.feature.arena.Arena;
import us.alleypvp.practice.feature.arena.ArenaService;
import us.alleypvp.practice.feature.arena.ArenaType;
import us.alleypvp.practice.feature.arena.ArenaValidator;
import us.alleypvp.practice.feature.arena.internal.swm.ArenaCopyManager;
import us.alleypvp.practice.feature.arena.internal.swm.NoOpArenaCopyManager;
import us.alleypvp.practice.feature.arena.internal.types.EventArena;
import us.alleypvp.practice.feature.arena.internal.types.FreeForAllArena;
import us.alleypvp.practice.feature.arena.internal.types.SharedArena;
import us.alleypvp.practice.feature.arena.internal.types.StandAloneArena;
import us.alleypvp.practice.feature.arena.schematic.ArenaSchematicService;
import us.alleypvp.practice.feature.kit.Kit;
import us.alleypvp.practice.feature.kit.KitService;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Getter
@Service(provides = ArenaService.class, priority = 110)
public final class ArenaServiceImpl implements ArenaService {
    private final AlleyPractice plugin;
    private final ConfigService configService;
    private final KitService kitService;
    private final ArenaSchematicService arenaSchematicService;
    private ArenaCopyManager arenaCopyManager;
    private final ExecutorService executorService;

    private final List<Arena> arenas = new ArrayList<>();
    private final List<StandAloneArena> temporaryArenas = new ArrayList<>();
    private final AtomicInteger copyIdCounter = new AtomicInteger(0);

    private final Map<String, List<Arena>> arenasByKit = new ConcurrentHashMap<>();
    private final Map<String, Arena> arenasByName = new ConcurrentHashMap<>();

    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    private World temporaryWorld;
    private Location nextCopyLocation;
    private final int arenaSpacing = 1500;

    private final ArenaValidator arenaValidator = new ArenaValidator();

    public ArenaServiceImpl(AlleyPractice plugin, ConfigService configService, KitService kitService, ArenaSchematicService arenaSchematicService) {
        this.plugin = plugin;
        this.configService = configService;
        this.kitService = kitService;
        this.arenaSchematicService = arenaSchematicService;
        this.arenaCopyManager = new NoOpArenaCopyManager();
        this.executorService = Executors.newFixedThreadPool(4);
    }

    @Override
    public void initialize(KaosContext context) {
        this.loadArenas();
        this.arenaSchematicService.generateMissingSchematics(this.arenas);
        this.arenaCopyManager = this.createArenaCopyManager();
        this.arenaCopyManager.initialize();

        if (!this.arenaCopyManager.isEnabled()) {
            this.initializeTemporaryWorld();
        }

        buildCaches();
    }

    @Override
    public void shutdown(KaosContext context) {
        cleanupTemporaryArenas();

        this.arenaCopyManager.shutdown();

        if (temporaryWorld != null) {
            String worldName = temporaryWorld.getName();

            temporaryWorld.getPlayers().forEach(player ->
                    player.teleport(Bukkit.getServer().getWorlds().get(0).getSpawnLocation())
            );

            if (Bukkit.unloadWorld(temporaryWorld, false)) {
                Logger.info("Mundo temporário descarregado com sucesso: " + worldName);
                File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
                if (worldFolder.exists()) {
                    FileUtil.deleteWorldFolder(worldFolder);
                    Logger.info("Deleted temporary world folder: " + worldName);
                }
            } else {
                Logger.error("Falha ao descarregar o mundo temporário: " + worldName);
            }
            temporaryWorld = null;
        }

        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    private void buildCaches() {
        arenasByKit.clear();
        arenasByName.clear();

        for (Kit kit : kitService.getKits()) {
            List<Arena> kitArenas = arenas.stream()
                    .filter(arena -> arena.getType() != ArenaType.EVENT)
                    .filter(arena -> arena.getKits().contains(kit.getName()))
                    .filter(Arena::isEnabled)
                    .collect(Collectors.toList());
            arenasByKit.put(kit.getName(), kitArenas);
        }

        for (Arena arena : arenas) {
            arenasByName.put(arena.getName().toLowerCase(), arena);
        }
    }

    private void initializeTemporaryWorld() {
        String worldName = "temporary_arena_world";
        cleanupExistingWorld(worldName);

        WorldCreator creator = new WorldCreator(worldName);
        creator.generateStructures(false).generator(new VoidChunkGenerator());

        this.temporaryWorld = creator.createWorld();
        this.nextCopyLocation = new Location(temporaryWorld, 0, 100, 0);
    }

    public void loadArenas() {
        FileConfiguration config = configService.getArenasConfig();
        ConfigurationSection arenasConfig = config.getConfigurationSection("arenas");

        if (arenasConfig == null) {
            return;
        }

        Set<String> arenaNames = arenasConfig.getKeys(false);

        if (arenaNames.size() <= 5) {
            for (String arenaName : arenaNames) {
                Arena arena = loadSingleArena(config, arenaName);
                if (arena != null) {
                    this.arenas.add(arena);
                }
            }
            return;
        }

        List<CompletableFuture<Arena>> futures = new ArrayList<>();

        for (String arenaName : arenaNames) {
            CompletableFuture<Arena> future = CompletableFuture.supplyAsync(() -> loadSingleArena(config, arenaName), executorService);
            futures.add(future);
        }

        for (CompletableFuture<Arena> future : futures) {
            try {
                Arena arena = future.get(5, TimeUnit.SECONDS);
                if (arena != null) {
                    this.arenas.add(arena);
                }
            } catch (TimeoutException e) {
                Logger.error("O carregamento da arena excedeu o tempo limite após 5 segundos");
                future.cancel(true);
            } catch (Exception e) {
                Logger.error("Falha ao carregar a arena: " + e.getMessage());
            }
        }
    }

    private Arena loadSingleArena(FileConfiguration config, String arenaName) {
        try {
            String name = "arenas." + arenaName;

            ArenaType arenaType = ArenaType.valueOf(config.getString(name + ".type"));
            Location minimum = Serializer.deserializeLocation(config.getString(name + ".minimum"));
            Location maximum = Serializer.deserializeLocation(config.getString(name + ".maximum"));

            Arena arena = createArenaByType(arenaType, arenaName, minimum, maximum, config, name);
            configureArena(arena, config, name);

            return arena;
        } catch (Exception e) {
            Logger.error("Error loading arena " + arenaName + ": " + e.getMessage());
            return null;
        }
    }

    private Arena createArenaByType(ArenaType arenaType, String arenaName,
                                    Location minimum, Location maximum,
                                    FileConfiguration config, String name) {
        switch (arenaType) {
            case SHARED:
                return new SharedArena(arenaName, minimum, maximum);

            case STANDALONE:
                int heightLimit = config.getInt(name + ".height-limit", 7);
                int voidLevel = config.getInt(name + ".void-level", 0);
                return new StandAloneArena(
                        arenaName, minimum, maximum,
                        Serializer.deserializeLocation(config.getString(name + ".team-one-portal")),
                        Serializer.deserializeLocation(config.getString(name + ".team-two-portal")),
                        heightLimit, voidLevel
                );

            case FFA:
                return new FreeForAllArena(
                        arenaName,
                        Serializer.deserializeLocation(config.getString(name + ".safe-zone.pos1")),
                        Serializer.deserializeLocation(config.getString(name + ".safe-zone.pos2"))
                );
            case EVENT:
                return new EventArena(arenaName, minimum, maximum);

            default:
                throw new IllegalStateException("Tipo de arena inesperado: " + arenaType);
        }
    }

    private void configureArena(Arena arena, FileConfiguration config, String name) {
        if (config.contains(name + ".kits")) {
            Set<String> validKits = new HashSet<>();
            for (String kitName : config.getStringList(name + ".kits")) {
                if (kitService.getKit(kitName) != null) {
                    validKits.add(kitName);
                }
            }
            arena.getKits().addAll(validKits);
        }

        if (config.contains(name + ".events")) {
            Set<String> validEvents = config.getStringList(name + ".events").stream()
                    .filter(Objects::nonNull)
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());
            arena.getEvents().addAll(validEvents);
        }

        if (config.contains(name + ".pos1")) {
            arena.setPos1(Serializer.deserializeLocation(config.getString(name + ".pos1")));
        }
        if (config.contains(name + ".pos2")) {
            arena.setPos2(Serializer.deserializeLocation(config.getString(name + ".pos2")));
        }
        if (config.contains(name + ".center")) {
            arena.setCenter(Serializer.deserializeLocation(config.getString(name + ".center")));
        }
        if (config.contains(name + ".display-name")) {
            arena.setDisplayName(config.getString(name + ".display-name"));
        }
        if (config.contains(name + ".enabled")) {
            arena.setEnabled(config.getBoolean(name + ".enabled"));
        }
    }

    @Override
    public StandAloneArena createTemporaryArenaCopy(StandAloneArena originalArena) {
        if (originalArena.isTemporaryCopy()) {
            throw new IllegalArgumentException("Cannot create a temporary copy of a temporary arena.");
        }

        int copyId = copyIdCounter.incrementAndGet();

        if (this.arenaCopyManager.isEnabled()) {
            StandAloneArena copiedArena = this.arenaCopyManager.createTemporaryArenaCopy(originalArena, copyId);
            if (copiedArena != null) {
                this.temporaryArenas.add(copiedArena);

                if (copiedArena.getPos1() != null) {
                    copiedArena.getPos1().getChunk().load(true);
                }
                if (copiedArena.getPos2() != null) {
                    copiedArena.getPos2().getChunk().load(true);
                }

                return copiedArena;
            }

            Logger.error("Falha ao criar cópia temporária SWM para '" + originalArena.getName() + "'. Voltando para colagem por schematic.");

            if (this.temporaryWorld == null) {
                this.initializeTemporaryWorld();
            }
        }

        Location copyLocation = getNextCopyLocationForArena(originalArena);

        Location originalPos1 = originalArena.getPos1();
        Location originalMin = originalArena.getMinimum();
        Location originalMax = originalArena.getMaximum();

        if (originalPos1 != null && originalMin != null && originalMax != null) {
            int actualMinY = Math.min(originalMin.getBlockY(), originalMax.getBlockY());
            int pos1OffsetFromActualMin = originalPos1.getBlockY() - actualMinY;
            int targetMinY = 100 - pos1OffsetFromActualMin;
            copyLocation.setY(targetMinY);
        }

        StandAloneArena copiedArena = originalArena.createCopy(temporaryWorld, copyLocation, copyId);
        if (copiedArena.getPos1() != null) {
            copiedArena.setHeightLimit(copiedArena.getPos1().getBlockY() + copiedArena.getHeightLimit());
        }

        this.arenaSchematicService.paste(copyLocation, this.arenaSchematicService.getSchematicFile(originalArena.getName()));

        if (copiedArena.getPos1() != null) {
            copiedArena.getPos1().getChunk().load(true);
        }
        if (copiedArena.getPos2() != null) {
            copiedArena.getPos2().getChunk().load(true);
        }

        this.temporaryArenas.add(copiedArena);
        return copiedArena;
    }

    public Location getNextCopyLocationForArena(StandAloneArena originalArena) {
        Location location = this.nextCopyLocation.clone();

        Location originalPos1 = originalArena.getPos1();
        Location originalMin = originalArena.getMinimum();

        if (originalPos1 != null && originalMin != null) {
            int pos1OffsetFromMin = originalPos1.getBlockY() - originalMin.getBlockY();
            location.setY(100 - pos1OffsetFromMin);
        }

        nextCopyLocation.add(arenaSpacing, 0, 0);
        if (nextCopyLocation.getX() > arenaSpacing * 10) {
            nextCopyLocation.setX(0);
            nextCopyLocation.add(0, 0, arenaSpacing);
        }

        return location;
    }

    public void cleanupTemporaryArenas() {
        for (StandAloneArena arena : new ArrayList<>(temporaryArenas)) {
            if (!this.arenaCopyManager.deleteTemporaryArena(arena)) {
                arena.deleteCopiedArena();
            }
        }
        this.temporaryArenas.clear();
    }

    private void cleanupExistingWorld(String worldName) {
        World existingWorld = this.plugin.getServer().getWorld(worldName);
        if (existingWorld != null) {
            existingWorld.getPlayers().forEach(player ->
                    player.teleport(Bukkit.getServer().getWorlds().get(0).getSpawnLocation())
            );

            boolean unloaded = this.plugin.getServer().unloadWorld(existingWorld, false);
            if (!unloaded) {
                Logger.error("Falha ao descarregar o mundo: " + worldName);
            }
        }

        File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
        if (worldFolder.exists()) {
            FileUtil.deleteWorldFolder(worldFolder);
        }
    }

    @Override
    public List<Arena> getArenas() {
        return Collections.unmodifiableList(arenas);
    }

    @Override
    public List<StandAloneArena> getTemporaryArenas() {
        return Collections.unmodifiableList(temporaryArenas);
    }

    @Override
    public void saveArena(Arena arena) {
        if (arena == null) {
            return;
        }

        arena.saveArena();
        buildCaches();
    }

    @Override
    public void deleteArena(Arena arena) {
        if (arena == null) {
            return;
        }

        arena.deleteArena();
        arenas.remove(arena);
        buildCaches();
    }

    @Override
    public void deleteTemporaryArena(StandAloneArena arena) {
        if (arena == null || !temporaryArenas.contains(arena)) {
            return;
        }

        if (!this.arenaCopyManager.deleteTemporaryArena(arena)) {
            arena.deleteCopiedArena();
        }

        this.temporaryArenas.remove(arena);
    }

    @Override
    public Arena getRandomArena(Kit kit) {
        List<Arena> availableArenas = arenasByKit.get(kit.getName());
        if (availableArenas == null || availableArenas.isEmpty()) {
            return null;
        }

        Arena selectedArena = availableArenas.get(random.nextInt(availableArenas.size()));
        if (selectedArena instanceof StandAloneArena) {
            return createTemporaryArenaCopy((StandAloneArena) selectedArena);
        }
        return selectedArena;
    }

    @Override
    public Arena getArenaByName(String name) {
        return arenasByName.get(name.toLowerCase());
    }

    @Override
    public Arena selectArenaWithPotentialTemporaryCopy(Arena arena) {
        if (arena instanceof StandAloneArena) {
            return createTemporaryArenaCopy((StandAloneArena) arena);
        }
        return arena;
    }

    @Override
    public void registerNewArena(Arena arena) {
        if (arena != null && !arenasByName.containsKey(arena.getName().toLowerCase())) {
            this.arenas.add(arena);
            this.buildCaches();
        }
    }

    public void refreshCaches() {
        CompletableFuture.runAsync(this::buildCaches, executorService);
    }

    private ArenaCopyManager createArenaCopyManager() {
        FileConfiguration settings = this.configService.getSettingsConfig();
        if (settings == null || !settings.getBoolean("arena-management.swm.enabled", false)) {
            return new NoOpArenaCopyManager();
        }

        Plugin swmPlugin = Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        if (swmPlugin == null) {
            Logger.error("SWM está ativado no settings.yml, mas o plugin SlimeWorldManager não está carregado. Usando fallback por schematic.");
            return new NoOpArenaCopyManager();
        }

        try {
            Class<?> swmManagerClass = Class.forName("us.alleypvp.practice.feature.arena.internal.swm.SwmArenaManager");
            Class<?> swmManagerClassDef = Class.forName("us.alleypvp.practice.feature.arena.internal.swm.SwmArenaManager");
            Constructor<?> constructor = swmManagerClassDef.getConstructor(AlleyPractice.class, ConfigService.class, ArenaSchematicService.class);
            Object managerInstance = constructor.newInstance(this.plugin, this.configService, this.arenaSchematicService);

            if (managerInstance instanceof ArenaCopyManager) {
                return (ArenaCopyManager) managerInstance;
            }

            Logger.error("SwmArenaManager não implementa ArenaCopyManager. Usando fallback por schematic.");
        } catch (Throwable throwable) {
            Exception exceptionToLog = (throwable instanceof Exception)
                    ? (Exception) throwable
                    : new Exception(throwable);

            Logger.logException("Falha ao carregar SwmArenaManager. Usando fallback por schematic.", exceptionToLog);
        }

        return new NoOpArenaCopyManager();
    }
}