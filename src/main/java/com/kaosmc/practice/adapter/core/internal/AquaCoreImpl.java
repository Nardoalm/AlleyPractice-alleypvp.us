package com.kaosmc.practice.adapter.core.internal;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.adapter.core.Core;
import com.kaosmc.practice.adapter.core.CoreType;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.SettingsLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.feature.level.LevelService;
import me.activated.core.api.tags.Tag;
import me.activated.core.plugin.AquaCoreAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * @author Emmy
 * @project Kaos
 * @since 26/04/2025
 */
public class AquaCoreImpl implements Core {
    protected final KaosPractice plugin;
    protected final AquaCoreAPI aquaCoreAPI;

    /**
     * Constructor for the AquaCoreImpl class.
     *
     * @param aquaCoreAPI The AquaCoreAPI instance to use.
     * @param plugin      The Kaos bootstrap instance.
     */
    public AquaCoreImpl(AquaCoreAPI aquaCoreAPI, KaosPractice plugin) {
        this.aquaCoreAPI = aquaCoreAPI;
        this.plugin = plugin;
    }

    @Override
    public CoreType getType() {
        return CoreType.AQUA;
    }

    @Override
    public ChatColor getPlayerColor(Player player) {
        return this.aquaCoreAPI.getPlayerNameColor(player.getUniqueId());
    }

    @Override
    public String getRankPrefix(Player player) {
        return this.aquaCoreAPI.getPlayerRank(player.getUniqueId()).getPrefix();
    }

    @Override
    public String getRankSuffix(Player player) {
        return this.aquaCoreAPI.getPlayerRank(player.getUniqueId()).getSuffix();
    }

    @Override
    public String getRankName(Player player) {
        return this.aquaCoreAPI.getPlayerRank(player.getUniqueId()).getName();
    }

    @Override
    public ChatColor getRankColor(Player player) {
        return this.aquaCoreAPI.getPlayerRank(player.getUniqueId()).getColor();
    }

    @Override
    public String getTagPrefix(Player player) {
        Tag tag = this.aquaCoreAPI.getTag(player.getUniqueId());
        if (tag == null) {
            return "";
        }

        String prefix = tag.getPrefix();
        if (prefix == null) {
            return "";
        }

        return prefix;
    }

    @Override
    public ChatColor getTagColor(Player player) {
        Tag tag = this.aquaCoreAPI.getTag(player.getUniqueId());
        if (tag == null) {
            return ChatColor.RESET;
        }

        ChatColor color = tag.getColor();
        if (color == null) {
            return ChatColor.RESET;
        }

        return color;
    }

    @Override
    public String getChatFormat(Player player, String eventMessage, String separator) {
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        LocaleService localeService = KaosPractice.getInstance().getService(LocaleService.class);

        Profile profile = profileService.getProfile(player.getUniqueId());
        String prefix = CC.translate(this.getRankPrefix(player));
        String suffix = CC.translate(this.getRankSuffix(player));
        ChatColor nameColor = profile.getNameColor() != null ? profile.getNameColor() : this.getPlayerColor(player);

        String selectedTitle = CC.translate(profile.getProfileData().getSelectedTitle());
        String level = CC.translate(KaosPractice.getInstance().getService(LevelService.class).getLevel(profile.getProfileData().getGlobalLevel()).getDisplayName());

        String tagAppearanceFormat = localeService.getString(SettingsLocaleImpl.SERVER_CHAT_FORMAT_TAG_APPEARANCE_FORMAT)
                .replace("{tag-color}", String.valueOf(this.getTagColor(player)))
                .replace("{tag-prefix}", CC.translate(this.getTagPrefix(player)));

        if (player.hasPermission(localeService.getString(SettingsLocaleImpl.PERMISSION_USE_OF_COLOR_CODES_IN_CHAT))) {
            eventMessage = CC.translate(eventMessage);
        }

        return localeService.getString(SettingsLocaleImpl.SERVER_CHAT_FORMAT_GLOBAL)
                .replace("{prefix}", prefix)
                .replace("{rank-color}", String.valueOf(this.getRankColor(player)))
                .replace("{name-color}", String.valueOf(nameColor))
                .replace("{player}", player.getName())
                .replace("{suffix}", suffix)
                .replace("{tag}", this.getTagPrefix(player).isEmpty() ? "" : tagAppearanceFormat)
                .replace("{separator}", separator)
                .replace("{message}", eventMessage)
                .replace("{level}", Objects.requireNonNull(CC.translate(level), "Level cannot be null"))
                .replace("{selected-title}", selectedTitle);
    }
}