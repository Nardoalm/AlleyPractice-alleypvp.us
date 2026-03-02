package com.kaosmc.practice.feature.duel.command;

import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.feature.duel.menu.DuelRequestsMenu;
import com.kaosmc.practice.feature.server.ServerService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @date 22/10/2024 - 18:19
 */
public class DuelRequestsCommand extends BaseCommand {
    @CommandData(
            name = "duelrequests",
            aliases = {"viewduelrequests", "viewrequests"},
            usage = "duelrequests",
            description = "View your duel requests"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (this.plugin.getService(ProfileService.class).getProfile(player.getUniqueId()).getMatch() != null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_ALREADY_PLAYING_MATCH));
            return;
        }

        ServerService serverService = this.plugin.getService(ServerService.class);
        if (!serverService.isQueueingAllowed()) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.QUEUE_TEMPORARILY_DISABLED));
            player.closeInventory();
            return;
        }

        new DuelRequestsMenu().openMenu(player);
    }
}