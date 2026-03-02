package com.kaosmc.practice.core.profile.command.player;

import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.menu.match.MatchHistorySelectKitMenu;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @date 14/09/2024 - 23:05
 */
public class MatchHistoryCommand extends BaseCommand {
    @CommandData(
            name = "matchhistory",
            aliases = {"pastmatches", "previousmatches", "mh"},
            usage = "matchhistory",
            description = "View your match history"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = this.getProfile(player.getUniqueId());
        if (profile.isBusy()) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_MUST_BE_IN_LOBBY));
            return;
        }

        if (profile.getProfileData().getPreviousMatches().isEmpty()) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_NO_MATCH_HISTORY));
            return;
        }

        new MatchHistorySelectKitMenu().openMenu(player);
    }
}