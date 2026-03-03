package com.kaosmc.practice.common.logger.command;

import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import com.kaosmc.practice.common.logger.Logger;
import com.kaosmc.practice.common.text.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Emmy
 * @project Kaos
 * @since 03/04/2025
 */
public class ViewErrorCommand extends BaseCommand {
    @CommandData(
            name = "viewerror",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "viewerror <error>",
            description = "Vê um erro registrado pelo ID."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (sender instanceof Player) {
            sender.sendMessage(CC.translate("&cThis command can only be used in console."));
            return;
        }

        if (args.length == 0) {
            command.sendUsage();
            return;
        }

        try {
            UUID errorId = UUID.fromString(args[0]);
            Logger.viewException(errorId);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(CC.translate("&cInvalid error ID!"));
        }
    }
}