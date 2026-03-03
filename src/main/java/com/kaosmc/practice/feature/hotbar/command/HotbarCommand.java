package com.kaosmc.practice.feature.hotbar.command;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;

import java.util.Arrays;

/**
 * @author Emmy
 * @project kaos-practice
 * @since 21/07/2025
 */
public class HotbarCommand extends BaseCommand {
    @Override
    public void onCommand(CommandArgs command) {
        Arrays.asList(
                "",
                "&c&l[NOTE] &fThese commands are not implemented yet.",
                "",
                "&6&lHotbar Commands Help",
                " &f◆ &6/hotbar create &8(&7name&8) &8(&7type&8) &7| Cria um novo item de hotbar.",
                " &f◆ &6/hotbar delete &8(&7name&8) &7| Delete a hotbar item.",
                " &f◆ &6/hotbar list &7| List all hotbar items.",
                ""
        ).forEach(line -> command.getSender().sendMessage(CC.translate(line)));

        //TODO: implement the actual commands
    }
}
