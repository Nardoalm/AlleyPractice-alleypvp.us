package com.kaosmc.practice.feature.division.command.impl.player;

import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import com.kaosmc.practice.feature.division.menu.DivisionsMenu;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Kaos
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