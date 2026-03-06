package com.kaosmc.practice.common;

import com.kaosmc.practice.common.text.CC;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Utility methods for resolving player-facing identity and visibility placeholders.
 */
@UtilityClass
public class PlayerDisplayUtil {
    private static final String NICK_PLACEHOLDER = "%kaoscore_player-nick%";
    private static final String VANISH_PLACEHOLDER = "%kaoscore_player-isvanish%";

    public String resolveDisplayName(Player player) {
        String fallback = player != null ? player.getName() : "Unknown";
        return resolveDisplayName(player, fallback);
    }

    public String resolveDisplayName(Player player, String fallback) {
        String safeFallback = sanitizeFallback(fallback);
        if (player == null) {
            return safeFallback;
        }

        String resolved = PlaceholderUtil.setPapiSafe(player, NICK_PLACEHOLDER);
        if (resolved == null) {
            return safeFallback;
        }

        String cleaned = CC.translate(resolved).trim();
        if (cleaned.isEmpty() || cleaned.equalsIgnoreCase(NICK_PLACEHOLDER)) {
            return safeFallback;
        }

        return cleaned;
    }

    public boolean isVanished(Player player) {
        if (player == null) {
            return false;
        }

        String resolved = PlaceholderUtil.setPapiSafe(player, VANISH_PLACEHOLDER);
        if (resolved == null) {
            return false;
        }

        String cleaned = ChatColor.stripColor(CC.translate(resolved)).trim();
        if (cleaned.isEmpty() || cleaned.equalsIgnoreCase(VANISH_PLACEHOLDER)) {
            return false;
        }

        return cleaned.equalsIgnoreCase("true")
                || cleaned.equalsIgnoreCase("yes")
                || cleaned.equalsIgnoreCase("on")
                || cleaned.equals("1");
    }

    private String sanitizeFallback(String fallback) {
        if (fallback == null || fallback.trim().isEmpty()) {
            return "Unknown";
        }

        return fallback;
    }
}
