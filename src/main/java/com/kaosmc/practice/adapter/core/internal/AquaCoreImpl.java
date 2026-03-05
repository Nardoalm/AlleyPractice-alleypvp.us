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
import com.kaosmc.practice.feature.level.data.LevelData;
import me.activated.core.api.tags.Tag;
import me.activated.core.plugin.AquaCoreAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AquaCoreImpl implements Core {
    protected final KaosPractice plugin;
    protected final AquaCoreAPI aquaCoreAPI;

    public AquaCoreImpl(AquaCoreAPI aquaCoreAPI, KaosPractice plugin) {
        this.aquaCoreAPI = aquaCoreAPI;
        this.plugin = plugin;
    }

    @Override public CoreType getType() { return CoreType.AQUA; }

    @Override
    public ChatColor getPlayerColor(Player player) {
        ChatColor color = this.aquaCoreAPI.getPlayerNameColor(player.getUniqueId());
        return color != null ? color : ChatColor.WHITE;
    }

    @Override
    public String getRankPrefix(Player player) {
        if (this.aquaCoreAPI.getPlayerRank(player.getUniqueId()) == null) return "";
        return CC.translate(this.aquaCoreAPI.getPlayerRank(player.getUniqueId()).getPrefix());
    }

    @Override
    public String getRankSuffix(Player player) {
        if (this.aquaCoreAPI.getPlayerRank(player.getUniqueId()) == null) return "";
        return CC.translate(this.aquaCoreAPI.getPlayerRank(player.getUniqueId()).getSuffix());
    }

    @Override public String getRankName(Player player) { return "Member"; }
    @Override public ChatColor getRankColor(Player player) { return ChatColor.WHITE; }

    @Override
    public String getTagPrefix(Player player) {
        Tag tag = this.aquaCoreAPI.getTag(player.getUniqueId());
        return (tag != null && tag.getPrefix() != null) ? tag.getPrefix() : "";
    }

    @Override
    public ChatColor getTagColor(Player player) {
        Tag tag = this.aquaCoreAPI.getTag(player.getUniqueId());
        return (tag != null && tag.getColor() != null) ? tag.getColor() : ChatColor.RESET;
    }

    @Override
    public String getChatFormat(Player player, String eventMessage, String separator) {
        ProfileService ps = KaosPractice.getInstance().getService(ProfileService.class);
        LocaleService ls = KaosPractice.getInstance().getService(LocaleService.class);
        LevelService levS = KaosPractice.getInstance().getService(LevelService.class);

        Profile profile = ps.getProfile(player.getUniqueId());
        if (profile == null || profile.getProfileData() == null) return player.getName() + ": " + eventMessage;

        String levelDisplay = "";
        if (levS != null && profile.getProfileData().getGlobalLevel() != null) {
            LevelData ld = levS.getLevel(profile.getProfileData().getGlobalLevel());
            if (ld != null) levelDisplay = CC.translate(ld.getDisplayName());
        }

        String prefix = getRankPrefix(player);
        String suffix = getRankSuffix(player);
        ChatColor nColor = profile.getNameColor() != null ? profile.getNameColor() : getPlayerColor(player);

        String format = ls.getString(SettingsLocaleImpl.SERVER_CHAT_FORMAT_GLOBAL);
        return CC.translate(format
                .replace("{prefix}", prefix)
                .replace("{player}", player.getName())
                .replace("{suffix}", suffix)
                .replace("{message}", eventMessage)
                .replace("{level}", levelDisplay)
                .replace("{name-color}", nColor.toString())
                .replace("{separator}", separator != null ? separator : ":"));
    }
}