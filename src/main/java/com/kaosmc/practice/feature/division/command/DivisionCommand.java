package com.kaosmc.practice.feature.division.command;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.feature.division.menu.DivisionsMenu;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Kaos
 * @date 6/2/2024
 */
public class DivisionCommand extends BaseCommand {
    @CommandData(
            name = "division",
            usage = "division",
            description = "Envia uma lista de comandos para gerenciar divisões"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        //TODO: make page based

        if (player.hasPermission("kaos.admin")) {
            player.sendMessage(" ");
            player.sendMessage(CC.translate("&6&lAjuda dos Comandos de Divisão:"));
            player.sendMessage(CC.translate(" &f◆ &6/division create &8(&7divisionName&8) &8(&7requiredWins&8) &7| Criar uma divisão"));
            player.sendMessage(CC.translate(" &f◆ &6/division delete &8(&7divisionName&8) &7| Excluir uma divisão"));
            player.sendMessage(CC.translate(" &f◆ &6/division view &8(&7divisionName&8) &7| Ver informações da divisão"));
            player.sendMessage(CC.translate(" &f◆ &6/division setwins &8(&7divisionName&8) &8(&7requiredWins&8) &8(&7tier&8) &7| Definir vitórias necessárias de um tier"));
            player.sendMessage(CC.translate(" &f◆ &6/division seticon &8(&7divisionName&8) &7| Definir ícone da divisão"));
            player.sendMessage(CC.translate(" &f◆ &6/division setdisplayname &8(&7divisionName&8) &8(&7displayName&8) &7| Definir nome de exibição da divisão"));
            player.sendMessage(CC.translate(" &f◆ &6/division setdescription &8(&7divisionName&8) &8(&7description&8) &7| Definir descrição da divisão"));
            player.sendMessage(CC.translate(" &f◆ &6/division menu &7| Abrir o menu de divisão"));
            player.sendMessage(CC.translate(" &f◆ &6/division list &7| Ver todas as divisões"));
            player.sendMessage("");
            return;
        }

        new DivisionsMenu().openMenu(player);
    }
}
