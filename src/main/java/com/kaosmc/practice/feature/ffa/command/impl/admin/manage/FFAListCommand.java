package com.kaosmc.practice.feature.ffa.command.impl.admin.manage;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.feature.ffa.FFAService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Kaos
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
        player.sendMessage(CC.translate("     &6&lFFA Match List &f(" + ffaService.getMatches().size() + "&f)"));
        if (ffaService.getMatches().isEmpty()) {
            player.sendMessage(CC.translate("      &f◆ &cNenhuma partida disponível."));
        }
        ffaService.getMatches().forEach(match -> player.sendMessage(CC.translate("      &f◆ &6" + match.getKit().getDisplayName() + " &f(" + (match.getPlayers().size() + "/" + match.getMaxPlayers()) + "&f)")));
        player.sendMessage("");
    }
}