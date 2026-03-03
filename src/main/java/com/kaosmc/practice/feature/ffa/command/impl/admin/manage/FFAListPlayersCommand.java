package com.kaosmc.practice.feature.ffa.command.impl.admin.manage;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.ffa.FFAMatch;
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
public class FFAListPlayersCommand extends BaseCommand {
    @CommandData(
            name = "ffa.listplayers",
            isAdminOnly = true,
            usage = "ffa listplayers <kit>",
            description = "Lista todos os jogadores de uma partida de FFA."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            command.sendUsage();
            return;
        }

        String kitName = args[0];
        FFAMatch match = this.plugin.getService(FFAService.class).getFFAMatch(kitName);
        if (match == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.FFA_NOT_FOUND).replace("{ffa-name}", kitName));
            return;
        }

        player.sendMessage("");
        player.sendMessage(CC.translate("     &6&l" + match.getKit().getDisplayName() + " Player List &f(" + match.getPlayers().size() + "&f)"));
        if (match.getPlayers().isEmpty()) {
            player.sendMessage(CC.translate("      &f◆ &cNenhum jogador disponível."));
        }
        match.getPlayers().forEach(participant -> player.sendMessage(CC.translate("      &f◆ &6" + participant.getName())));
        player.sendMessage("");
    }
}
