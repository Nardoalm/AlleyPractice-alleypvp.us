package com.kaosmc.practice.feature.division.command.impl.manage;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.division.Division;
import com.kaosmc.practice.feature.division.DivisionService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Kaos
 * @since 26/01/2025
 */
public class DivisionViewCommand extends BaseCommand {
    @CommandData(
            name = "division.view",
            isAdminOnly = true,
            usage = "division view <name>",
            description = "View information about a division."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        DivisionService divisionService = this.plugin.getService(DivisionService.class);
        Division division = divisionService.getDivision(args[0]);
        if (division == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.DIVISION_NOT_FOUND).replace("{division-name}", args[0]));
            return;
        }

        Arrays.asList(
                "",
                "&6&lDivision &f(" + division.getDisplayName() + ")",
                " &f◆ &6Name: &f" + division.getDisplayName(),
                " &f◆ &6Tiers: &f" + division.getTiers().size(),
                " &f◆ &6Description: &f" + division.getDescription(),
                " &f◆ &6Required Wins: &f" + division.getTiers().get(0).getRequiredWins(),
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));
    }
}