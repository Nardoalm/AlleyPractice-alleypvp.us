package com.kaosmc.practice.feature.command.impl.other.gamemode;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @since 13/06/2025
 */
public class SpectatorCommand extends BaseCommand {
    @CommandData(
            name = "gmsp",
            aliases = {"gm.sp", "gamemode.sp", "gm.3", "gm3", "gamemode.3", "gamemode.spectator"},
            isAdminOnly = true,
            usage = "gamemode spectator [player]",
            description = "Sets your or another player's gamemode to Spectator."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(CC.translate("&eSeu modo de jogo foi alterado para Espectador."));
            return;
        }

        Player targetPlayer = this.plugin.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        targetPlayer.setGameMode(GameMode.SPECTATOR);
        player.sendMessage(CC.translate("&eYou have updated &d" + targetPlayer.getName() + "'s &egamemode to Spectator."));
        targetPlayer.sendMessage(CC.translate("&eSeu modo de jogo foi alterado para Espectador."));
    }
}