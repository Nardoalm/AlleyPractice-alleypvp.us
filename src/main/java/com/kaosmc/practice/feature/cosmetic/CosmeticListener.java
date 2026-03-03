package com.kaosmc.practice.feature.cosmetic;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Remi
 * @project kaos-practice
 * @date 4/08/2025
 */
public class CosmeticListener implements Listener {
    private static final Map<UUID, Long> lastMoveTimes = new ConcurrentHashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() ||
                event.getFrom().getBlockY() != event.getTo().getBlockY() ||
                event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            lastMoveTimes.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        lastMoveTimes.remove(event.getPlayer().getUniqueId());
    }

    /**
     * Checks if a player has been standing still longer than the specified delay.
     *
     * @param player      The player to check.
     * @param delayMillis The required delay in milliseconds.
     * @return true if the player is considered still.
     */
    public static boolean isPlayerStill(Player player, long delayMillis) {
        return (System.currentTimeMillis() - lastMoveTimes.getOrDefault(player.getUniqueId(), 0L)) > delayMillis;
    }
}
