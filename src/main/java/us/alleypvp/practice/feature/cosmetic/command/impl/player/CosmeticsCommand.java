package us.alleypvp.practice.feature.cosmetic.command.impl.player;

import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import us.alleypvp.practice.feature.cosmetic.menu.CosmeticsMenu;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
public class CosmeticsCommand extends BaseCommand {
    @CommandData(
            name = "cosmetics",
            usage = "cosmetics",
            description = "Abre o menu de cosméticos"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new CosmeticsMenu().openMenu(player);
    }
}