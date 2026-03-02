package com.kaosmc.practice.feature.command.impl.other;

import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @date 22/06/2025
 */
public class SudoAllCommand extends BaseCommand {
    @CommandData(
            name = "sudoall",
            isAdminOnly = true,
            usage = "sudoall <message>",
            description = "Make all players say a message"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        String message = String.join(" ", args);
        for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
            onlinePlayer.chat(message);
        }

        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.OTHER_SUDO_ALL_PLAYERS).replace("{message}", message));
    }
}
