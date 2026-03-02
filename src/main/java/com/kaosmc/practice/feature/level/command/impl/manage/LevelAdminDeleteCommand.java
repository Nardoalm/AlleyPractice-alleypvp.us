package com.kaosmc.practice.feature.level.command.impl.manage;

import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.level.LevelService;
import com.kaosmc.practice.feature.level.data.LevelData;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Kaos
 * @since 26/05/2025
 */
public class LevelAdminDeleteCommand extends BaseCommand {
    @CommandData(
            name = "leveladmin.delete",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "leveladmin delete <levelName>",
            description = "Delete a level"
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        String levelName = args[0];
        LevelService levelService = this.plugin.getService(LevelService.class);
        LevelData level = levelService.getLevel(levelName);
        if (level == null) {
            sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.LEVEL_NOT_FOUND).replace("{level-name}", levelName));
            return;
        }

        levelService.deleteLevel(level);
        sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.LEVEL_DELETED).replace("{level-name}", levelName));
    }
}