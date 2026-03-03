package com.kaosmc.practice.feature.division.command.impl.data;

import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.division.Division;
import com.kaosmc.practice.feature.division.DivisionService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @since 28/01/2025
 */
public class DivisionSetDisplayNameCommand extends BaseCommand {
    @CommandData(
            name = "division.setdisplayname",
            isAdminOnly = true,
            usage = "division setdisplayname <name> <displayName>",
            description = "Define o nome de exibição de uma divisão."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            command.sendUsage();
            return;
        }

        DivisionService divisionService = this.plugin.getService(DivisionService.class);
        Division division = divisionService.getDivision(args[0]);
        if (division == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.DIVISION_NOT_FOUND).replace("{division-name}", args[0]));
            return;
        }

        String displayName = args[1];
        division.setDisplayName(displayName);
        divisionService.saveDivision(division);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.DIVISION_DISPLAY_NAME_SET)
                .replace("{division-name}", division.getName())
                .replace("{display-name}", displayName)
        );
    }
}