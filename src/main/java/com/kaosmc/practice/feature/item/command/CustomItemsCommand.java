package com.kaosmc.practice.feature.item.command;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;

import java.util.Arrays;

/**
 * @author Emmy
 * @project kaos-practice
 * @since 18/07/2025
 */
public class CustomItemsCommand extends BaseCommand {
    @CommandData(
            name = "customitems",
            aliases = {"kaositems", "specialitems"},
            usage = "customitems",
            description = "Lista de comandos para itens especiais",
            isAdminOnly = true
    )
    @Override
    public void onCommand(CommandArgs command) {
        Arrays.asList(
                "",
                "&6&lCustom Items Commands Help:",
                " &f◆ &6/customitems goldenhead &8(&7amount&8) &7| Gives you a golden head",
                ""
        ).forEach(line -> command.getPlayer().sendMessage(CC.translate(line)));
    }
}