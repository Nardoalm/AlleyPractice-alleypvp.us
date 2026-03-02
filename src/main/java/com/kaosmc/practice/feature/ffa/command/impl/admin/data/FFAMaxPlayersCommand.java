package com.kaosmc.practice.feature.ffa.command.impl.admin.data;

import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
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
public class FFAMaxPlayersCommand extends BaseCommand {
    @CommandData(
            name = "ffa.maxplayers",
            isAdminOnly = true,
            usage = "ffa maxplayers <kit> <maxPlayers>",
            description = "Set the max players for a FFA kit."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 2) {
            command.sendUsage();
            return;
        }

        String kitName = args[0];
        int maxPlayers = Integer.parseInt(args[1]);

        FFAMatch match = this.plugin.getService(FFAService.class).getFFAMatch(kitName);
        if (match == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.FFA_NOT_FOUND).replace("{ffa-name}", kitName));
            return;
        }

        match.setMaxPlayers(maxPlayers);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.FFA_MAX_PLAYERS_SET)
                .replace("{kit-name}", kitName)
                .replace("{max-players}", String.valueOf(maxPlayers))
        );
    }
}