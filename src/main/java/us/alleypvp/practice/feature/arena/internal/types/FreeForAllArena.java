package us.alleypvp.practice.feature.arena.internal.types;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.feature.arena.Arena;
import us.alleypvp.practice.feature.arena.ArenaService;
import us.alleypvp.practice.feature.arena.ArenaType;
import us.alleypvp.practice.core.config.ConfigService;
import us.alleypvp.practice.common.serializer.Serializer;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Emmy
 * @project Alley
 * @date 20/05/2024 - 19:15
 */
public class FreeForAllArena extends Arena {
    protected final AlleyPractice plugin = AlleyPractice.getInstance();

    /**
     * Constructor for the FreeForAllArena class.
     *
     * @param name    The name of the arena.
     * @param minimum The minimum location of the arena.
     * @param maximum The maximum location of the arena.
     */
    public FreeForAllArena(String name, Location minimum, Location maximum) {
        super(name, minimum, maximum);
    }

    @Override
    public ArenaType getType() {
        return ArenaType.FFA;
    }

    @Override
    public void createArena() {
        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        arenaService.registerNewArena(this);
        this.saveArena();
    }

    @Override
    public void saveArena() {
        String name = "arenas." + this.getName();
        FileConfiguration config = this.plugin.getService(ConfigService.class).getArenasConfig();

        config.set(name, null);
        config.set(name + ".type", this.getType().name());
        config.set(name + ".safe-zone.pos1", Serializer.serializeLocation(this.getMinimum()));
        config.set(name + ".safe-zone.pos2", Serializer.serializeLocation(this.getMaximum()));
        config.set(name + ".center", Serializer.serializeLocation(this.getCenter()));
        config.set(name + ".pos1", Serializer.serializeLocation(this.getPos1()));
        config.set(name + ".events", this.getEvents());
        config.set(name + ".enabled", this.isEnabled());
        config.set(name + ".display-name", this.getDisplayName());

        this.plugin.getService(ConfigService.class).saveConfig(this.plugin.getService(ConfigService.class).getConfigFile("storage/arenas.yml"), config);
    }

    @Override
    public void deleteArena() {
        FileConfiguration config = this.plugin.getService(ConfigService.class).getArenasConfig();
        config.set("arenas." + this.getName(), null);

        this.plugin.getService(ConfigService.class).saveConfig(this.plugin.getService(ConfigService.class).getConfigFile("storage/arenas.yml"), config);
    }
}
