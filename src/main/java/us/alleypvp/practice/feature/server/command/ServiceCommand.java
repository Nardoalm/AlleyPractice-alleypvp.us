package us.alleypvp.practice.feature.server.command;

import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 09/03/2025
 */
public class ServiceCommand extends BaseCommand {
    @CommandData(
            name = "service",
            isAdminOnly = true,
            usage = "service",
            description = "Comando de serviço."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Arrays.asList(
                "",
                "&b&lService Commands",
                " &f◆ &b/service menu &7| &fOpens the service menu.",
                " &f◆ &b/service allowqueue &8(&7true/false&8) &7| &fPermite/bloqueia filas.",
                " &f◆ &b/service togglecrafting &7| &fEnable/Disable crafting for an item.",
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));
    }
}