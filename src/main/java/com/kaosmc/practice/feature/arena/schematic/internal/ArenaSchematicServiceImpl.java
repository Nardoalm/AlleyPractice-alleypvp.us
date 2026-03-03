package com.kaosmc.practice.feature.arena.schematic.internal;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.feature.arena.Arena;
import com.kaosmc.practice.feature.arena.internal.types.StandAloneArena;
import com.kaosmc.practice.bootstrap.KaosContext;
import com.kaosmc.practice.bootstrap.annotation.Service;
import com.kaosmc.practice.common.logger.Logger;
import com.kaosmc.practice.feature.arena.schematic.ArenaSchematicService;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

/**
 * @author Remi
 * @project kaos-practice
 * @since 02/07/2025
 */
@Service(provides = ArenaSchematicService.class, priority = 120)
public class ArenaSchematicServiceImpl implements ArenaSchematicService {
    private final KaosPractice plugin;
    private File schematicsDirectory;

    /**
     * Constructor for the ArenaSchematicServiceImpl class.
     *
     * @param plugin The main Kaos bootstrap instance.
     */
    public ArenaSchematicServiceImpl(KaosPractice plugin) {
        this.plugin = plugin;
    }

    @Override
    public void setup(KaosContext context) {
        this.schematicsDirectory = this.getSchematicsDirectory();
        this.createSchematicsFolder();
    }

    private void createSchematicsFolder() {
        File schematicsDir = this.getSchematicsDirectory();
        if (!schematicsDir.exists()) {
            if (schematicsDir.mkdirs()) {
                Logger.info("Created schematics directory: " + schematicsDir.getPath());
            } else {
                Logger.error("Failed to create schematics directory: " + schematicsDir.getPath());
            }
        }
    }

    @Override
    public void generateMissingSchematics(List<Arena> arenas) {
        for (Arena arena : arenas) {
            File schematicFile = getSchematicFile(arena);
            if (!schematicFile.exists()) {
                Logger.info("Schematic for " + arena.getName() + " not found, creating...");
                save(arena, schematicFile);
            }
        }
    }

    @Override
    public void save(Arena arena, File schematicFile) {
        try {
            Location min = arena.getMinimum();
            Location max = arena.getMaximum();

            World bukkitWorld = min.getWorld();
            CuboidSelection selection = new CuboidSelection(bukkitWorld, min, max);

            Vector minVector = BukkitUtil.toVector(selection.getMinimumPoint());
            Vector maxVector = BukkitUtil.toVector(selection.getMaximumPoint());

            CuboidRegion region = new CuboidRegion(minVector, maxVector);
            BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
            clipboard.setOrigin(minVector);

            EditSession session = new EditSession(BukkitUtil.getLocalWorld(bukkitWorld), -1);
            session.setFastMode(true);

            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(session, region, clipboard, region.getMinimumPoint());
            Operations.complete(forwardExtentCopy);

            try (ClipboardWriter writer = ClipboardFormat.SCHEMATIC.getWriter(Files.newOutputStream(schematicFile.toPath()))) {
                writer.write(clipboard, session.getWorld().getWorldData());
            } catch (Exception e) {
                Logger.logException("Failed to write schematic to file: " + schematicFile.getPath(), e);
            }

            Logger.info("Saved schematic for arena: " + arena.getName());
        } catch (Exception exception) {
            Logger.logException("Failed to save schematic for arena " + arena.getName(), exception);
        }
    }

    @Override
    public void updateSchematic(Arena arena) {
        File schematicFile = getSchematicFile(arena.getName());
        this.save(arena, schematicFile);
    }

    /**
     * Pastes the schematic at the specified location.
     *
     * @param location      The location to paste the schematic.
     * @param schematicFile The file containing the schematic to paste.
     */
    public void paste(Location location, File schematicFile) {
        if (!schematicFile.exists()) {
            Logger.error("Cannot paste schematic, file does not exist: " + schematicFile.getPath());
            return;
        }

        try {
            World bukkitWorld = location.getWorld();
            Vector toVector = BukkitUtil.toVector(location);
            EditSession session = new EditSession(BukkitUtil.getLocalWorld(bukkitWorld), -1);
            session.setFastMode(true);

            Schematic schema = FaweAPI.load(schematicFile);
            schema.paste(session, toVector, false);

            session.flushQueue();
        } catch (Exception exception) {
            Logger.logException("Failed to paste schematic at " + location, exception);
        }
    }

    @Override
    public void delete(StandAloneArena arena) {
        if (!arena.isTemporaryCopy()) {
            return;
        }

        try {
            Location min = arena.getMinimum();
            Location max = arena.getMaximum();

            if (min == null || max == null || min.getWorld() == null) {
                Logger.error("Cannot delete arena '" + arena.getName() + "': Invalid bounds.");
                return;
            }

            World bukkitWorld = min.getWorld();
            BukkitWorld world = new BukkitWorld(bukkitWorld);

            Vector minVector = BukkitUtil.toVector(min);
            Vector maxVector = BukkitUtil.toVector(max);

            EditSession session = new EditSession(world, -1);
            session.setFastMode(true);

            session.setBlocks(new CuboidRegion(minVector, maxVector), new BaseBlock(0));
            session.flushQueue();
        } catch (Exception exception) {
            Logger.logException("Failed to delete arena " + arena.getName(), exception);
        }
    }

    @Override
    public File getSchematicFile(String name) {
        return new File(getSchematicsDirectory() + File.separator + name.toLowerCase().replace(" ", "_") + ".schematic");
    }

    @Override
    public File getSchematicFile(Arena arena) {
        return this.getSchematicFile(arena.getName());
    }

    private File getSchematicsDirectory() {
        return new File(this.plugin.getDataFolder(), "schematics");
    }
}