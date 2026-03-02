package com.kaosmc.practice.feature.level.command.impl.manage;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.feature.level.LevelService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Kaos
 * @since 26/05/2025
 */
public class LevelAdminListCommand extends BaseCommand {
    @CommandData(
            name = "leveladmin.list",
            isAdminOnly = true,
            usage = "level admin list",
            description = "List all levels.",
            inGameOnly = false
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        LevelService levelService = this.plugin.getService(LevelService.class);

        sender.sendMessage("");
        sender.sendMessage(CC.translate("     &6&lLevel List &f(" + levelService.getLevels().size() + "&f)"));
        if (levelService.getLevels().isEmpty()) {
            sender.sendMessage(CC.translate("      &f◆ &cNo levels available."));
        } else {
            levelService.getLevels()
                    .forEach(level -> sender.sendMessage(CC.translate("      &f◆ &6" + level.getDisplayName() + " &f(" + level.getMinElo() + " - " + level.getMaxElo() + " elo)")));
        }
        sender.sendMessage("");
    }
}