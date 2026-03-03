package com.kaosmc.practice.core.profile.data.command;

import com.kaosmc.practice.common.PlayerUtil;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.menu.reset.ResetConfirmMenu;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Emmy
 * @project Kaos
 * @date 02/01/2025 - 20:58
 */
public class ResetStatsCommand extends BaseCommand {
    @CommandData(
            name = "resetstats",
            aliases = {"wipestats",},
            isAdminOnly = true,
            usage = "resetstats <player>",
            description = "Reseta as estatísticas de um jogador."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        Player onlineTarget = Bukkit.getPlayerExact(args[0]);
        OfflinePlayer target = onlineTarget != null ? onlineTarget : PlayerUtil.getOfflinePlayerByName(args[0]);

        UUID uuid = target.getUniqueId();
        if (uuid == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        Profile profile = this.plugin.getService(ProfileService.class).getProfile(uuid);
        if (profile == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        new ResetConfirmMenu(uuid).openMenu(player);
    }
}