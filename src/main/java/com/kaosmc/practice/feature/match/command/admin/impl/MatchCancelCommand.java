package com.kaosmc.practice.feature.match.command.admin.impl;

import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.enums.ProfileState;
import com.kaosmc.practice.feature.match.MatchState;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Kaos
 * @date 5/26/2024
 */
public class MatchCancelCommand extends BaseCommand {
    @CommandData(
            name = "match.cancel",
            isAdminOnly = true,
            usage = "match cancel <player>",
            description = "Cancela à força a partida de um jogador."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            command.sendUsage();
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        Profile profile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());
        if (profile.getState() != ProfileState.PLAYING || profile.getMatch() == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_PLAYER_NOT_PLAYING_MATCH)
                    .replace("{name-color}", String.valueOf(profile.getNameColor()))
                    .replace("{player}", target.getName())
            );
            return;
        }

        profile.getMatch().handleRoundEnd();
        profile.getMatch().setState(MatchState.ENDING_MATCH);
        profile.getMatch().getRunnable().setStage(4);

        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.MATCH_CANCELLED_FOR_PLAYER)
                .replace("{name-color}", String.valueOf(profile.getNameColor()))
                .replace("{player}", target.getName())
        );
    }
}
