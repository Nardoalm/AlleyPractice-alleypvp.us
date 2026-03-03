package com.kaosmc.practice.feature.cosmetic.command.impl.player;

import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import com.kaosmc.practice.feature.cosmetic.menu.CosmeticsMenu;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Kaos
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