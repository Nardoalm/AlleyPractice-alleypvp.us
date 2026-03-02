package com.kaosmc.practice.feature.server.command.impl;

import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import com.kaosmc.practice.feature.server.menu.ServiceMenu;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @since 09/03/2025
 */
public class ServiceMenuCommand extends BaseCommand {
    @CommandData(name = "service.menu", isAdminOnly = true, usage = "service menu", description = "Service menu command.")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new ServiceMenu().openMenu(player);
    }
}
