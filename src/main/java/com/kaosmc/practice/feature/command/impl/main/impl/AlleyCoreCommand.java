package com.kaosmc.practice.feature.command.impl.main.impl;

import com.kaosmc.practice.adapter.core.Core;
import com.kaosmc.practice.adapter.core.CoreAdapter;
import com.kaosmc.practice.adapter.core.CoreType;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import com.kaosmc.practice.common.text.CC;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Kaos
 * @since 26/04/2025
 */
public class AlleyCoreCommand extends BaseCommand {
    @CommandData(
            name = "alley.core",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "alley core",
            description = "Displays information about the core hook."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        Core core = this.plugin.getService(CoreAdapter.class).getCore();

        Arrays.asList(
                "",
                "&6&lCore Hook Information",
                " &6&l│ &rPlugin: &6" + core.getType().getPluginName(),
                //" &6&l│ &rVersion: &6" + core.getType().getPluginVersion(),
                " &6&l│ &rAuthors: &6" + core.getType().getPluginAuthor(),
                ""
        ).forEach(line -> sender.sendMessage(CC.translate(line)));

        if (core.getType() == CoreType.DEFAULT) {
            sender.sendMessage(CC.translate("&7Note: This is the default server implementation, as there was no server found to hook into."));
        }
    }
}
