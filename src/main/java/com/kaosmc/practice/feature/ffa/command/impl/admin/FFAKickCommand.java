package com.kaosmc.practice.feature.ffa.command.impl.admin;

import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.feature.ffa.FFAMatch;
import com.kaosmc.practice.feature.ffa.FFAService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Kaos
 * @date 5/27/2024
 */
public class FFAKickCommand extends BaseCommand {
    @CommandData(
            name = "ffa.kick",
            isAdminOnly = true,
            usage = "ffa kick <player>",
            description = "Kick a player from their current FFA match."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            command.sendUsage();
            return;
        }

        Player targetPlayer = player.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        FFAMatch match = this.plugin.getService(FFAService.class).getFFAMatch(targetPlayer);
        if (match == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_PLAYER_NOT_PLAYING_FFA)
                    .replace("{name-color}", String.valueOf(this.getProfile(targetPlayer.getUniqueId()).getNameColor()))
                    .replace("{player}", targetPlayer.getName()));
            return;
        }

        match.leave(targetPlayer);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.FFA_KICKED_PLAYER)
                .replace("{player}", targetPlayer.getName())
                .replace("{ffa-name}", match.getName())
                .replace("{name-color}", String.valueOf(this.plugin.getService(ProfileService.class).getProfile(targetPlayer.getUniqueId()).getNameColor()))
        );
    }
}