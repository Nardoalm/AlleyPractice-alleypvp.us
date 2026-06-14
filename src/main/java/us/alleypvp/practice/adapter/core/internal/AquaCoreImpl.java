package us.alleypvp.practice.adapter.core.internal;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.adapter.core.Core;
import us.alleypvp.practice.adapter.core.CoreType;
import us.alleypvp.practice.common.text.CC;
import me.activated.core.api.tags.Tag;
import me.activated.core.plugin.AquaCoreAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
public class AquaCoreImpl implements Core {
    protected final AlleyPractice plugin;
    protected final AquaCoreAPI aquaCoreAPI;

    public AquaCoreImpl(AquaCoreAPI aquaCoreAPI, AlleyPractice plugin) {
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
        return Core.super.getChatFormat(player, eventMessage, separator);
    }
}
