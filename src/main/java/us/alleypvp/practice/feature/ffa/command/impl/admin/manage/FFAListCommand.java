package us.alleypvp.practice.feature.ffa.command.impl.admin.manage;

import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.feature.ffa.FFAService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class FFAListCommand extends BaseCommand {
    @CommandData(
            name = "ffa.list",
            isAdminOnly = true,
            usage = "ffa list",
            description = "Envia uma lista de todas as partidas de FFA."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        FFAService ffaService = this.plugin.getService(FFAService.class);

        player.sendMessage("");
        player.sendMessage(CC.translate("     &b&lFFA Match List &f(" + ffaService.getMatches().size() + "&f)"));
        if (ffaService.getMatches().isEmpty()) {
            player.sendMessage(CC.translate("      &f◆ &cNenhuma partida disponível."));
        }
        ffaService.getMatches().forEach(match -> player.sendMessage(CC.translate("      &f◆ &b" + match.getKit().getDisplayName() + " &f(" + (match.getPlayers().size() + "/" + match.getMaxPlayers()) + "&f)")));
        player.sendMessage("");
    }
}