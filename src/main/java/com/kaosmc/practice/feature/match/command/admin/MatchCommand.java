package com.kaosmc.practice.feature.match.command.admin;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.command.CommandSender;

/**
 * @author Remi
 * @project Kaos
 * @date 5/26/2024
 */
public class MatchCommand extends BaseCommand {
    @CommandData(
            name = "match",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "match",
            description = "Manage matches"
    )
    @Override
    public void onCommand(CommandArgs args) {
        CommandSender sender = args.getSender();

        sender.sendMessage(" ");
        sender.sendMessage(CC.translate("&6&lMatch Commands Help:"));
        sender.sendMessage(CC.translate(" &f◆ &6/match start &8(&7p1&8) &8(&7p2&8) &8(&7arena&8) &8(&7kit&8) &7| Start a match"));
        sender.sendMessage(CC.translate(" &f◆ &6/match cancel &8(&7player&8) &7| Cancel a match"));
        sender.sendMessage(CC.translate(" &f◆ &6/match info &8(&7player&8) &7| Get match info of a player"));
        sender.sendMessage("");
    }
}
