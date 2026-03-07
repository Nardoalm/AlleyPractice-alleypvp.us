package com.kaosmc.practice.visual.nametag;

import com.kaosmc.practice.common.logger.Logger;
import com.kaosmc.practice.common.reflect.Reflection;
import com.kaosmc.practice.common.reflect.internal.types.DefaultReflectionImpl;
import com.kaosmc.practice.visual.nametag.internal.NametagServiceImpl;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Remi
 * @project kaos-practice
 * @date 27/06/2025
 */
@Getter
public class NametagAdapter {
    private final NametagServiceImpl engine;
    private final String name;
    private final int sortWeight;
    private final String prefix;
    private final String suffix;
    private final NametagVisibility visibility;
    private final String adapterKey;
    private final Reflection reflection = DefaultReflectionImpl.INSTANCE;

    public NametagAdapter(NametagServiceImpl engine, String name, int sortWeight, String prefix, String suffix, NametagVisibility visibility, String adapterKey) {
        this.engine = engine;
        this.name = name;
        this.sortWeight = sortWeight;
        this.prefix = prefix;
        this.suffix = suffix;
        this.visibility = visibility;
        this.adapterKey = adapterKey != null ? adapterKey : "";
    }

    /**
     * Checks if this adapter represents the same style as a NametagView.
     *
     * @param view The view to compare against.
     * @return True if the prefix and suffix match.
     */
    public boolean represents(NametagView view) {
        return this.prefix.equals(view.getPrefix())
                && this.suffix.equals(view.getSuffix())
                && this.visibility == view.getVisibility()
                && this.sortWeight == view.getSortWeight()
                && this.adapterKey.equals(view.getAdapterKey());
    }

    /**
     * Sends the team creation packet to a specific player.
     *
     * @param player The player to send the packet to.
     */
    public void sendCreationPacket(Player player) {
        createPacket(0).sendToPlayer(player);
    }

    /**
     * Adds a player to this team for a specific viewer.
     *
     * @param player The player to add to the team.
     * @param viewer The player who needs to see this change.
     */
    public void addPlayer(Player player, Player viewer) {
        createPacket(3, player.getName()).sendToPlayer(viewer);
    }

    /**
     * Removes a player from this team for a specific viewer.
     *
     * @param player The player to remove from the team.
     * @param viewer The player who needs to see this change.
     */
    public void removePlayer(Player player, Player viewer) {
        createPacket(4, player.getName()).sendToPlayer(viewer);
    }

    @SuppressWarnings("unchecked")
    private PacketWrapper createPacket(int mode, String... players) {
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
        try {
            reflection.getField(PacketPlayOutScoreboardTeam.class, "a").set(packet, name);
            reflection.getField(PacketPlayOutScoreboardTeam.class, "h").set(packet, mode);
            if (mode == 0 || mode == 2) {
                reflection.getField(PacketPlayOutScoreboardTeam.class, "b").set(packet, name);
                reflection.getField(PacketPlayOutScoreboardTeam.class, "c").set(packet, prefix);
                reflection.getField(PacketPlayOutScoreboardTeam.class, "d").set(packet, suffix);
                reflection.getField(PacketPlayOutScoreboardTeam.class, "i").set(packet, 3);
                reflection.getField(PacketPlayOutScoreboardTeam.class, "e").set(packet, visibility.getValue());
            }
            if (mode == 0 || mode == 3 || mode == 4) {
                ((Collection<String>) reflection.getField(PacketPlayOutScoreboardTeam.class, "g").get(packet)).addAll(Arrays.asList(players));
            }
        } catch (Exception e) {
            Logger.logException("Failed to create nametag packet", e);
        }
        return new PacketWrapper(packet, reflection);
    }

    /**
     * A simple, private static wrapper to make sending packets cleaner.
     */
    private static final class PacketWrapper {
        private final PacketPlayOutScoreboardTeam packet;
        private final Reflection reflectionService;

        public PacketWrapper(PacketPlayOutScoreboardTeam packet, Reflection reflectionService) {
            this.packet = packet;
            this.reflectionService = reflectionService;
        }

        void sendToPlayer(Player player) {
            if (player != null && player.isOnline()) {
                reflectionService.sendPacket(player, packet);
            }
        }
    }
}
