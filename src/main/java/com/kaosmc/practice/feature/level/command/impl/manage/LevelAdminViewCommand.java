package com.kaosmc.practice.feature.level.command.impl.manage;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.level.LevelService;
import com.kaosmc.practice.feature.level.data.LevelData;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Kaos
 * @since 26/05/2025
 */
public class LevelAdminViewCommand extends BaseCommand {
    @CommandData(
            name = "leveladmin.view",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "leveladmin view <levelName>",
            description = "Mostra informações do nível"
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

        Arrays.asList(
                "",
                "&6&lInformações do Nível:",
                " &f◆ &6Nome: &e" + level.getName(),
                " &f◆ &6Nome de Exibição: &e" + level.getDisplayName(),
                " &f◆ &6Elo Mínimo: &e" + level.getMinElo(),
                " &f◆ &6Elo Máximo: &e" + level.getMaxElo(),
                " &f◆ &6Material: &e" + level.getMaterial().name(),
                " &f◆ &6Durabilidade: &e" + level.getDurability(),
                ""
        ).forEach(line -> sender.sendMessage(CC.translate(line)));
    }
}
