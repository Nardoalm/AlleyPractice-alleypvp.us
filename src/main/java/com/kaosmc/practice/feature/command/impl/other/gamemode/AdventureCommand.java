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
public class AdventureCommand extends BaseCommand {
    @CommandData(
            name = "gma",
            aliases = {"gm.a", "gamemode.a", "gm.2", "gm2", "gamemode.2", "gamemode.adventure"},
            isAdminOnly = true,
            usage = "gamemode adventure [player]",
            description = "Define o modo de jogo seu ou de outro jogador para Aventura."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.setGameMode(GameMode.ADVENTURE);
            player.sendMessage(CC.translate("&eSeu modo de jogo foi alterado para Aventura."));
            return;
        }

        Player targetPlayer = this.plugin.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        targetPlayer.setGameMode(GameMode.ADVENTURE);
        player.sendMessage(CC.translate("&eYou have updated &d" + targetPlayer.getName() + "'s &egamemode to Adventure."));
        targetPlayer.sendMessage(CC.translate("&eSeu modo de jogo foi alterado para Aventura."));
    }
}