package com.kaosmc.practice.visual.nametag.internal;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kaosmc.practice.visual.nametag.NametagAdapter;
import com.kaosmc.practice.visual.nametag.NametagView;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

/**
 * @author Remi
 * @project kaos-practice
 * @date 27/06/2025
 */
@Getter
public class NametagRegistry {
    private final Cache<String, NametagAdapter> adapterCache;
    private final NametagServiceImpl service;

    public NametagRegistry(NametagServiceImpl service) {
        this.service = service;
        this.adapterCache = CacheBuilder.newBuilder()
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build();
    }

    /**
     * Gets or creates a NametagAdapter for a given style.
     *
     * @param view The nametag view.
     * @return The cached or newly created NametagAdapter.
     */
    public NametagAdapter getAdapter(NametagView view) {
        String key = view.getAdapterKey() + "|" + view.getSortWeight() + "|" + view.getPrefix() + "|" + view.getSuffix() + "|" + view.getVisibility().name();
        try {
            return adapterCache.get(key, () -> {
                int clampedWeight = Math.max(0, Math.min(9999, view.getSortWeight()));
                int styleHash = key.hashCode() & 0xFFFFFF;
                String teamName = String.format("nt%04d%06x", clampedWeight, styleHash);
                return new NametagAdapter(service, teamName, clampedWeight, view.getPrefix(), view.getSuffix(), view.getVisibility(), view.getAdapterKey());
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to load nametag adapter from cache", e);
        }
    }

    /**
     * Sends creation packets for all active adapters to a specific player.
     *
     * @param player The player to receive the packets.
     */
    public void sendAllAdapters(Player player) {
        for (NametagAdapter adapter : adapterCache.asMap().values()) {
            adapter.sendCreationPacket(player);
        }
    }

    /**
     * Cleans up a player's data from all perspectives when they quit.
     *
     * @param player The player who quit.
     */
    public void cleanupPlayer(Player player) {
        service.getPlayerPerspectives().values().forEach(p -> p.getDisplayedAdapters().remove(player.getUniqueId()));
    }
}
