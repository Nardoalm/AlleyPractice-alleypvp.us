package us.alleypvp.practice.feature.division.command.impl.player;

import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import us.alleypvp.practice.feature.division.menu.DivisionsMenu;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
public class DivisionsCommand extends BaseCommand {
    @CommandData(
            name = "divisions",
            aliases = {"division.menu"},
            usage = "divisions",
            description = "Abre o menu de divisões"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new DivisionsMenu().openMenu(player);
    }
}