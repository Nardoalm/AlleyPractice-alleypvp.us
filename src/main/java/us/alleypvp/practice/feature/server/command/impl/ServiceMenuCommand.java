package us.alleypvp.practice.feature.server.command.impl;

import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import us.alleypvp.practice.feature.server.menu.ServiceMenu;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 09/03/2025
 */
public class ServiceMenuCommand extends BaseCommand {
    @CommandData(name = "service.menu", isAdminOnly = true, usage = "service menu", description = "Comando do menu de serviço.")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new ServiceMenu().openMenu(player);
    }
}
