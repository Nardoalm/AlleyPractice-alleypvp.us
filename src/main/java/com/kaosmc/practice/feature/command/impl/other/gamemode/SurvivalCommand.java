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
public class SurvivalCommand extends BaseCommand {
    @CommandData(
            name = "gms",
            aliases = {"gm.s", "gamemode.s", "gm.0", "gm0", "gamemode.0", "gamemode.survival"},
            isAdminOnly = true,
            usage = "gamemode survival [player]",
            description = "Define o modo de jogo seu ou de outro jogador para Sobrevivência."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(CC.translate("&eSeu modo de jogo foi alterado para Sobrevivência."));
            return;
        }

        Player targetPlayer = this.plugin.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        targetPlayer.setGameMode(GameMode.SURVIVAL);
        player.sendMessage(CC.translate("&eYou have updated &d" + targetPlayer.getName() + "'s &egamemode to Survival."));
        targetPlayer.sendMessage(CC.translate("&eSeu modo de jogo foi alterado para Sobrevivência."));
    }
}