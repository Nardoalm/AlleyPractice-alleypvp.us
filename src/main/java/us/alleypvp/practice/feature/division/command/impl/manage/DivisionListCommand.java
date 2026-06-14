package us.alleypvp.practice.feature.division.command.impl.manage;

import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.feature.division.DivisionService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
public class DivisionListCommand extends BaseCommand {
    @CommandData(
            name = "division.list",
            isAdminOnly = true,
            usage = "division list",
            description = "Envia uma lista de todos os divisions."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        DivisionService divisionService = this.plugin.getService(DivisionService.class);

        player.sendMessage("");
        player.sendMessage(CC.translate("     &b&lDivision List &f(" + divisionService.getDivisions().size() + "&f)"));
        if (divisionService.getDivisions().isEmpty()) {
            player.sendMessage(CC.translate("      &f◆ &cNenhuma divisão disponível."));
        }
        divisionService.getDivisions()
                .forEach(division -> player.sendMessage(CC.translate("      &f◆ &b" + division.getDisplayName() + " &f(" + division.getTiers().get(0).getRequiredWins() + " wins)")));
        player.sendMessage("");

    }
}