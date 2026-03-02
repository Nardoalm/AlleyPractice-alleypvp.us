package com.kaosmc.practice.feature.match.command.admin.impl;

import com.kaosmc.practice.core.locale.internal.impl.message.GameMessagesLocaleImpl;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.feature.match.Match;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project Kaos
 * @since 16/06/2025
 */
public class MatchResetBlocksCommand extends BaseCommand {
    @CommandData(
            name = "match.resetblocks",
            isAdminOnly = true,
            usage = "match resetblocks",
            description = "Reset all block changes in your current match."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        Match match = profile.getMatch();
        if (match == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_NOT_PLAYING_MATCH));
            return;
        }

        match.resetBlockChanges();
        if (this.getBoolean(GameMessagesLocaleImpl.MATCH_BLOCKS_RESET_MESSAGE_ENABLED_BOOLEAN)) {
            List<String> messages = this.getStringList(GameMessagesLocaleImpl.MATCH_BLOCKS_RESET_MESSAGE_FORMAT);
            for (String message : messages) {
                match.sendMessage(message
                        .replace("{name-color}", String.valueOf(profile.getNameColor()))
                        .replace("{player}", player.getName())
                );
            }
        }
    }
}