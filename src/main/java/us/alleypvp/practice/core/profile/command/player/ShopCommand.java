package us.alleypvp.practice.core.profile.command.player;

import us.alleypvp.practice.core.profile.menu.shop.ShopMenu;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
public class ShopCommand extends BaseCommand {
    @CommandData(
            name = "shop",
            usage = "shop",
            description = "Abre a loja do servidor"
    )
    @Override
    public void onCommand(CommandArgs command) {
        new ShopMenu().openMenu(command.getPlayer());
    }
}