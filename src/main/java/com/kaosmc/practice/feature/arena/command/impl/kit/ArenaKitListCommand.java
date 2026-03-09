package com.kaosmc.practice.feature.arena.command.impl.kit;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.arena.Arena;
import com.kaosmc.practice.feature.arena.ArenaService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import com.kaosmc.practice.library.command.annotation.CompleterData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Kaos
 * @date 5/20/2024
 */
public class ArenaKitListCommand extends BaseCommand {
    @CompleterData(name = "arena.kitlist")
    public List<String> arenaKitListCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            this.plugin.getService(ArenaService.class).getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @CommandData(
            name = "arena.kitlist",
            isAdminOnly = true,
            usage = "arena kitlist <arenaName>",
            description = "Lista todos os kits associados a uma arena"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        String arenaName = args[0];
        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        Arena arena = arenaService.getArenaByName(arenaName);
        if (arena == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_NOT_FOUND).replace("{arena-name}", arenaName));
            return;
        }

        player.sendMessage("");
        player.sendMessage(CC.translate("     &6&lLista de Kits da Arena " + arenaName + " &f(" + arena.getKits().size() + "&f)"));
        if (arena.getKits().isEmpty()) {
            player.sendMessage(CC.translate("      &f◆ &cNenhum kit de arena disponível."));
        }
        arena.getKits().forEach(kit -> player.sendMessage(CC.translate("      &f◆ &6" + kit)));
        player.sendMessage("");
    }
}
