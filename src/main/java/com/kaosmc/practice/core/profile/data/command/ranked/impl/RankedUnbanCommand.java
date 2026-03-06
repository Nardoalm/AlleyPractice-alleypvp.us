package com.kaosmc.practice.core.profile.data.command.ranked.impl;

import com.kaosmc.practice.common.PlayerUtil;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project Kaos
 * @since 13/03/2025
 */
public class RankedUnbanCommand extends BaseCommand {
    @CommandData(
            name = "ranked.unban",
            isAdminOnly = true,
            usage = "ranked unban <player>",
            description = "Desbane um jogador das partidas ranked."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        String targetName = args[0];
        OfflinePlayer target = PlayerUtil.getOfflinePlayerByName(targetName);
        if (target == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        Profile profile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());
        if (profile == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        if (!profile.getProfileData().isRankedBanned()) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.RANKED_PLAYER_NOT_BANNED)
                    .replace("{name-color}", String.valueOf(profile.getNameColor()))
                    .replace("{player}", target.getName())
            );
            return;
        }

        profile.getProfileData().setRankedBanned(false);
        profile.save();
        if (this.getBoolean(GlobalMessagesLocaleImpl.RANKED_PLAYER_UNBAN_BROADCAST_BOOLEAN)) {
            List<String> message = this.getStringList(GlobalMessagesLocaleImpl.RANKED_PLAYER_UNBAN_BROADCAST);
            for (String line : message) {
                this.plugin.getServer().broadcastMessage(CC.translate(line
                        .replace("{name-color}", String.valueOf(profile.getNameColor()))
                        .replace("{player}", target.getName())
                        .replace("{reason}", "N/A")
                        .replace("{ban-id}", "N/A")
                        .replace("{duration}", "N/A") //TODO
                ));
            }
        }

        if (this.getBoolean(GlobalMessagesLocaleImpl.RANKED_UNBAN_MESSAGE_NOTICE_BOOLEAN)) {
            if (target.isOnline()) {
                Player targetPlayer = (Player) target;
                List<String> message = this.getStringList(GlobalMessagesLocaleImpl.RANKED_UNBAN_MESSAGE_NOTICE);
                for (String line : message) {
                    targetPlayer.sendMessage(line
                            .replace("{name-color}", String.valueOf(profile.getNameColor()))
                            .replace("{player}", target.getName())
                            .replace("{reason}", "N/A")
                            .replace("{ban-id}", "N/A")
                            .replace("{duration}", "N/A") //TODO
                    );
                }
            }
        }
    }
}
