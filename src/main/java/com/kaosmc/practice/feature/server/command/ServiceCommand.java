package com.kaosmc.practice.feature.server.command;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Kaos
 * @since 09/03/2025
 */
public class ServiceCommand extends BaseCommand {
    @CommandData(
            name = "service",
            isAdminOnly = true,
            usage = "service",
            description = "Service command."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Arrays.asList(
                "",
                "&6&lService Commands",
                " &f◆ &6/service menu &7| &fOpens the service menu.",
                " &f◆ &6/service allowqueue &8(&7true/false&8) &7| &fAllow/disallow queueing.",
                " &f◆ &6/service togglecrafting &7| &fEnable/Disable crafting for an item.",
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));
    }
}