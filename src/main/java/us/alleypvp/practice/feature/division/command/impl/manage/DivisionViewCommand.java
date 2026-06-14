package us.alleypvp.practice.feature.division.command.impl.manage;

import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.feature.division.Division;
import us.alleypvp.practice.feature.division.DivisionService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 26/01/2025
 */
public class DivisionViewCommand extends BaseCommand {
    @CommandData(
            name = "division.view",
            isAdminOnly = true,
            usage = "division view <name>",
            description = "Mostra informações de uma divisão."
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
                "&b&lDivision &f(" + division.getDisplayName() + ")",
                " &f◆ &bName: &f" + division.getDisplayName(),
                " &f◆ &bTiers: &f" + division.getTiers().size(),
                " &f◆ &bDescription: &f" + division.getDescription(),
                " &f◆ &bRequired Wins: &f" + division.getTiers().get(0).getRequiredWins(),
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));
    }
}