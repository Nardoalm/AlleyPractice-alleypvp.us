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

/**
 * @author Emmy
 * @project Kaos
 * @since 26/04/2025
 */
public class AquaCoreImpl implements Core {
    protected final KaosPractice plugin;
    protected final AquaCoreAPI aquaCoreAPI;

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
        if (player == null) return ChatColor.WHITE;
        ChatColor color = this.aquaCoreAPI.getPlayerNameColor(player.getUniqueId());
        return color != null ? color : ChatColor.WHITE;
    }

    @Override
    public String getRankPrefix(Player player) {
        if (player == null || this.aquaCoreAPI.getPlayerRank(player.getUniqueId()) == null) return "";
        String prefix = this.aquaCoreAPI.getPlayerRank(player.getUniqueId()).getPrefix();
        return prefix != null ? CC.translate(prefix) : "";
    }

    @Override
    public String getRankSuffix(Player player) {
        if (player == null || this.aquaCoreAPI.getPlayerRank(player.getUniqueId()) == null) return "";
        String suffix = this.aquaCoreAPI.getPlayerRank(player.getUniqueId()).getSuffix();
        return suffix != null ? CC.translate(suffix) : "";
    }

    @Override public String getRankName(Player player) { return "Member"; }
    @Override public ChatColor getRankColor(Player player) { return ChatColor.WHITE; }

    @Override
    public String getTagPrefix(Player player) {
        if (player == null) return "";
        Tag tag = this.aquaCoreAPI.getTag(player.getUniqueId());
        return (tag != null && tag.getPrefix() != null) ? tag.getPrefix() : "";
    }

    @Override
    public ChatColor getTagColor(Player player) {
        if (player == null) return ChatColor.RESET;
        Tag tag = this.aquaCoreAPI.getTag(player.getUniqueId());
        return (tag != null && tag.getColor() != null) ? tag.getColor() : ChatColor.RESET;
    }

    @Override
    public String getChatFormat(Player player, String eventMessage, String separator) {
        if (player == null) return eventMessage;

        ProfileService ps = KaosPractice.getInstance().getService(ProfileService.class);
        LocaleService ls = KaosPractice.getInstance().getService(LocaleService.class);
        LevelService levS = KaosPractice.getInstance().getService(LevelService.class);

        Profile profile = (ps != null) ? ps.getProfile(player.getUniqueId()) : null;

        // Fallback caso o perfil não exista ou o serviço falhe
        if (profile == null || profile.getProfileData() == null) {
            return player.getName() + (separator != null ? separator : ": ") + eventMessage;
        }

        String levelDisplay = "";
        if (levS != null && profile.getProfileData().getGlobalLevel() != null) {
            LevelData ld = levS.getLevel(profile.getProfileData().getGlobalLevel());
            if (ld != null && ld.getDisplayName() != null) {
                levelDisplay = CC.translate(ld.getDisplayName());
            }
        }

        String prefix = getRankPrefix(player);
        String suffix = getRankSuffix(player);

        // Prioridade: Cor do Perfil -> Cor do AquaCore -> Branco
        ChatColor nColor = profile.getNameColor();
        if (nColor == null) {
            nColor = getPlayerColor(player);
        }

        String format = (ls != null) ? ls.getString(SettingsLocaleImpl.SERVER_CHAT_FORMAT_GLOBAL) : "{prefix}{player}{suffix}{separator} {message}";

        return CC.translate(format
                .replace("{prefix}", prefix != null ? prefix : "")
                .replace("{player}", player.getName())
                .replace("{suffix}", suffix != null ? suffix : "")
                .replace("{message}", eventMessage != null ? eventMessage : "")
                .replace("{level}", levelDisplay)
                .replace("{name-color}", nColor != null ? nColor.toString() : ChatColor.WHITE.toString())
                .replace("{separator}", separator != null ? separator : ":"));

        // O código "unreachable" que estava aqui foi removido para o Maven compilar.
    }
}