package com.kaosmc.practice.adapter.core.internal;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.adapter.core.Core;
import com.kaosmc.practice.adapter.core.CoreType;
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
        return Core.super.getChatFormat(player, eventMessage, separator);
    }
}
