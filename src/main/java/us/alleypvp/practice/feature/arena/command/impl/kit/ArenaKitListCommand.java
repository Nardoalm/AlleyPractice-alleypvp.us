package us.alleypvp.practice.feature.arena.command.impl.kit;

import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.feature.arena.Arena;
import us.alleypvp.practice.feature.arena.ArenaService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import us.alleypvp.practice.library.command.annotation.CompleterData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
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
        player.sendMessage(CC.translate("     &b&lLista de Kits da Arena " + arenaName + " &f(" + arena.getKits().size() + "&f)"));
        if (arena.getKits().isEmpty()) {
            player.sendMessage(CC.translate("      &f◆ &cNenhum kit de arena disponível."));
        }
        arena.getKits().forEach(kit -> player.sendMessage(CC.translate("      &f◆ &b" + kit)));
        player.sendMessage("");
    }
}
