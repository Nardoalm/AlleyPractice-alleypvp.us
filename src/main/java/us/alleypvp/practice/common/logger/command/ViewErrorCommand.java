package us.alleypvp.practice.common.logger.command;

import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import us.alleypvp.practice.common.logger.Logger;
import us.alleypvp.practice.common.text.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
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
            sender.sendMessage(CC.translate("&cEste comando só pode ser usado no console."));
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
            sender.sendMessage(CC.translate("&cID de erro inválido!"));
        }
    }
}
