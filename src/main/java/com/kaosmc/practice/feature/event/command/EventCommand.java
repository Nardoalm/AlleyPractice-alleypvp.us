package com.kaosmc.practice.feature.event.command;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.feature.event.menu.EventMenu;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

public class EventCommand extends BaseCommand {
    @CommandData(
            name = "event",
            aliases = {"events"},
            usage = "event",
            description = "Abre o menu de eventos."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length > 0) {
            player.sendMessage(CC.translate("&6/event join &7- Entrar no evento ativo"));
            player.sendMessage(CC.translate("&6/event leave &7- Sair do evento atual"));
            player.sendMessage(CC.translate("&6/event start <evento> &7- Hospedar um evento"));
            player.sendMessage(CC.translate("&6/event stop &7- Encerrar o evento ativo"));
            return;
        }

        new EventMenu().openMenu(player);
    }
}
