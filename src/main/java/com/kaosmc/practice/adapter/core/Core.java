package com.kaosmc.practice.adapter.core;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.SettingsLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.feature.level.data.LevelData;
import com.kaosmc.practice.feature.level.LevelService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @since 26/04/2025
 */
public interface Core {
    /**
     * Retrieves the bootstrap name of the server implementation.
     *
     * @return The bootstrap name as a String.
     */
    CoreType getType();

    /**
     * Retrieves the color associated with a given player.
     *
     * @param player The player whose color is to be retrieved.
     * @return The color as a ChatColor object.
     */
    ChatColor getPlayerColor(Player player);

    /**
     * Retrieves the rank prefix for a given player
     *
     * @param player The player whose rank prefix is to be retrieved.
     * @return The rank prefix as a String.
     */
    String getRankPrefix(Player player);

    /**
     * Retrieves the rank name for a given player.
     *
     * @param player The player whose rank is to be retrieved.
     * @return The rank name as a String.
     */
    String getRankName(Player player);

    /**
     * Retrieves the rank suffix for a given player.
     *
     * @param player The player whose rank suffix is to be retrieved.
     * @return The rank suffix as a String.
     */
    String getRankSuffix(Player player);

    /**
     * Retrieves the rank color for a given player.
     *
     * @param player The player whose rank color is to be retrieved.
     * @return The rank color as a ChatColor object.
     */
    ChatColor getRankColor(Player player);

    /**
     * Retrieves the tag prefix for a given player.
     *
     * @param player The player whose tag prefix is to be retrieved.
     * @return The tag prefix as a String.
     */
    String getTagPrefix(Player player);

    /**
     * Retrieves the color associated with a given player's tag.
     *
     * @param player The player whose tag color is to be retrieved.
     * @return The tag color as a String.
     */
    ChatColor getTagColor(Player player);

    /**
     * Retrieves the chat format for a given player and message.
     *
     * @param player       The player whose chat format is to be retrieved.
     * @param eventMessage The message to be formatted.
     * @param separator    The separator to be used in the chat format.
     * @return The formatted chat message as a String.
     */
    default String getChatFormat(Player player, String eventMessage, String separator) {
        if (player == null) {
            return "";
        }

        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = null;
        if (profileService != null) {
            try {
                profile = profileService.getProfile(player.getUniqueId());
            } catch (Exception ignored) {
                // Fallback to default formatting below.
            }
        }
        LocaleService localeService = KaosPractice.getInstance().getService(LocaleService.class);
        LevelService levelService = KaosPractice.getInstance().getService(LevelService.class);

        String safeEventMessage = eventMessage != null ? eventMessage : "";
        String safeSeparator = separator != null ? separator : "";

        if (localeService == null) {
            return player.getName() + safeSeparator + " " + safeEventMessage;
        }

        String rawPrefix = "";
        String rawSuffix = "";
        String rawTagPrefix = "";
        try {
            rawPrefix = this.getRankPrefix(player);
        } catch (Exception ignored) {
        }
        try {
            rawSuffix = this.getRankSuffix(player);
        } catch (Exception ignored) {
        }
        try {
            rawTagPrefix = this.getTagPrefix(player);
        } catch (Exception ignored) {
        }

        String prefix = CC.translate(rawPrefix != null ? rawPrefix : "");
        String suffix = CC.translate(rawSuffix != null ? rawSuffix : "");
        String tagPrefix = CC.translate(rawTagPrefix != null ? rawTagPrefix : "");

        ChatColor fallbackPlayerColor;
        try {
            fallbackPlayerColor = this.getPlayerColor(player);
        } catch (Exception ignored) {
            fallbackPlayerColor = ChatColor.WHITE;
        }
        ChatColor nameColor = profile != null && profile.getNameColor() != null
                ? profile.getNameColor()
                : (fallbackPlayerColor != null ? fallbackPlayerColor : ChatColor.WHITE);
        ChatColor rankColor;
        ChatColor tagColor;
        try {
            rankColor = this.getRankColor(player);
        } catch (Exception ignored) {
            rankColor = ChatColor.WHITE;
        }
        try {
            tagColor = this.getTagColor(player);
        } catch (Exception ignored) {
            tagColor = ChatColor.WHITE;
        }

        if (rankColor == null) {
            rankColor = ChatColor.WHITE;
        }
        if (tagColor == null) {
            tagColor = ChatColor.WHITE;
        }

        String selectedTitle = "";
        String level = "Unranked";

        if (profile != null && profile.getProfileData() != null) {
            String rawTitle = profile.getProfileData().getSelectedTitle();
            selectedTitle = rawTitle != null ? CC.translate(rawTitle) : "";

            LevelData levelData = null;
            String globalLevel = profile.getProfileData().getGlobalLevel();

            if (levelService != null && globalLevel != null && !globalLevel.trim().isEmpty()) {
                try {
                    levelData = levelService.getLevel(globalLevel);
                } catch (Exception ignored) {
                    levelData = null;
                }
            }
            if (levelService != null && levelData == null) {
                try {
                    levelData = levelService.getLevel(profile.getProfileData().getElo());
                } catch (Exception ignored) {
                    levelData = null;
                }
            }
            if (levelData != null && levelData.getDisplayName() != null) {
                level = CC.translate(levelData.getDisplayName());
            }
        }

        String tagAppearanceTemplate = localeService.getString(SettingsLocaleImpl.SERVER_CHAT_FORMAT_TAG_APPEARANCE_FORMAT);
        if (tagAppearanceTemplate == null) {
            tagAppearanceTemplate = "{tag-prefix}";
        }

        String tagAppearanceFormat = tagAppearanceTemplate
                .replace("{tag-color}", String.valueOf(tagColor))
                .replace("{tag-prefix}", tagPrefix);

        String colorPermission = localeService.getString(SettingsLocaleImpl.PERMISSION_USE_OF_COLOR_CODES_IN_CHAT);
        if (colorPermission != null && !colorPermission.trim().isEmpty() && player.hasPermission(colorPermission)) {
            safeEventMessage = CC.translate(safeEventMessage);
        }

        String globalFormat = localeService.getString(SettingsLocaleImpl.SERVER_CHAT_FORMAT_GLOBAL);
        if (globalFormat == null) {
            globalFormat = "{prefix}{name-color}{player}{separator} {message}";
        }

        return globalFormat
                .replace("{prefix}", prefix)
                .replace("{rank-color}", String.valueOf(rankColor))
                .replace("{name-color}", String.valueOf(nameColor))
                .replace("{player}", player.getName())
                .replace("{suffix}", suffix)
                .replace("{tag}", tagPrefix.isEmpty() ? "" : tagAppearanceFormat)
                .replace("{separator}", safeSeparator)
                .replace("{message}", safeEventMessage)
                .replace("{level}", level)
                .replace("{nivel}", level)
                .replace("{nível}", level)
                .replace("{selected-title}", selectedTitle);
    }
}
