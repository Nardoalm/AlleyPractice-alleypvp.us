package com.kaosmc.practice.feature.kit.command.helper;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;

import java.util.Arrays;

/**
 * @author Emmy
 * @project kaos-practice
 * @since 14/07/2025
 */
public class KitHelperCommand extends BaseCommand {
    @CommandData(
            name = "kithelper",
            isAdminOnly = true,
            usage = "kithelper",
            description = "Fornece assistência para comandos utilitários."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Arrays.asList(
                "",
                "&6&lAjuda dos Comandos Auxiliares de Kit:",
                " &f◆ &6/enchant &8(&7enchantment&8) &8(&7level&8) &7| &fEncanta o item na mão.",
                " &f◆ &6/glow &8(&7true|false&8) &7| &fDefine o glow do item.",
                " &f◆ &6/potionduration &8(&7duration&8) &7| &fDefine a duração de uma poção.",
                " &f◆ &6/removeenchants &7| &fRemove os encantamentos do item.",
                " &f◆ &6/rename &8(&7name&8) &7| &fRenomeia o item na mão.",
                " &f◆ &6/unbreakable &8(&7true|false&8) &7| &fDefine o item como inquebrável.",
                ""
        ).forEach(message -> command.getSender().sendMessage(CC.translate(message)));
    }
}
