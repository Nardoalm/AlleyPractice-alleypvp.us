package com.kaosmc.practice.feature.level.command.impl.data;

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
public class LevelAdminSetMinEloCommand extends BaseCommand {
    @CommandData(
            name = "leveladmin.setminelo",
            isAdminOnly = true,
            usage = "leveladmin setminelo <levelName> <minElo>",
            description = "Set the minimum Elo for a level",
            inGameOnly = false
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
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

        int minElo;
        try {
            minElo = Integer.parseInt(args[1]);
        } catch (NumberFormatException exception) {
            sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_NUMBER).replace("{input}", args[1]));
            return;
        }

        if (minElo < 0) {
            sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.LEVEL_MINIMUM_ELO_CANNOT_BE_NEGATIVE));
            return;
        }

        if (minElo >= level.getMaxElo()) {
            sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.LEVEL_MINIMUM_ELO_MUST_BE_LESS_THAN_MAXIMUM));
            return;
        }

        level.setMinElo(minElo);
        levelService.saveLevel(level);

        sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.LEVEL_MINIMUM_ELO_SET)
                .replace("{level-name}", levelName)
                .replace("{min-elo}", String.valueOf(minElo))
        );
    }
}