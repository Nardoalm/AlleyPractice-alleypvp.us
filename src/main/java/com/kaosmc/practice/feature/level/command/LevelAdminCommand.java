package com.kaosmc.practice.feature.level.command;

import com.kaosmc.practice.common.constants.PluginConstant;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import com.kaosmc.practice.library.command.annotation.CompleterData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project Kaos
 * @since 26/05/2025
 */
public class LevelAdminCommand extends BaseCommand {
    @CompleterData(name = "leveladmin")
    public List<String> kitCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();
        if (command.getArgs().length != 1) {
            return completion;
        }

        if (!command.getPlayer().hasPermission(this.plugin.getService(PluginConstant.class).getAdminPermissionPrefix())) {
            return completion;
        }

        completion.addAll(Arrays.asList(
                "create", "delete", "view", "setminelo",
                "setmaxelo", "setdisplayname", "seticon", "list"
        ));

        return completion;
    }

    @CommandData(
            name = "leveladmin",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "leveladmin",
            description = "Comando administrativo para gerenciar níveis"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Arrays.asList(
                "",
                "&6&lLevel Admin Commands Help:",
                " &f◆ &6/leveladmin create &8(&7levelName&8) &8(&7minElo&8) &8(&7maxElo&8) &7| Cria um novo nível",
                " &f◆ &6/leveladmin delete &8(&7levelName&8) &7| Exclui um nível",
                " &f◆ &6/leveladmin list &7| List all levels",
                " &f◆ &6/leveladmin view &8(&7levelName&8) &7| View level info",
                " &f◆ &6/leveladmin setminelo &8(&7levelName&8) &8(&7minElo&8) &7| Set minimum Elo for a level",
                " &f◆ &6/leveladmin setmaxelo &8(&7levelName&8) &8(&7maxElo&8) &7| Set maximum Elo for a level",
                " &f◆ &6/leveladmin setdisplayname &8(&7levelName&8) &8(&7displayName&8) &7| Set display name for a level",
                " &f◆ &6/leveladmin seticon &8(&7levelName&8) &7| Set material for a level",
                ""
        ).forEach(line -> command.getSender().sendMessage(CC.translate(line)));
    }
}