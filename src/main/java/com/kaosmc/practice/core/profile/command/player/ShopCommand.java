package com.kaosmc.practice.core.profile.command.player;

import com.kaosmc.practice.core.profile.menu.shop.ShopMenu;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;

/**
 * @author Remi
 * @project Kaos
 * @date 6/2/2024
 */
public class ShopCommand extends BaseCommand {
    @CommandData(
            name = "shop",
            usage = "shop",
            description = "Open the server shop"
    )
    @Override
    public void onCommand(CommandArgs command) {
        new ShopMenu().openMenu(command.getPlayer());
    }
}