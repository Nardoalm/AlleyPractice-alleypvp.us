package us.alleypvp.practice.feature.arena.command.impl.kit;

import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.feature.arena.Arena;
import us.alleypvp.practice.feature.arena.ArenaService;
import us.alleypvp.practice.feature.kit.KitService;
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
public class ArenaRemoveKitCommand extends BaseCommand {
    @CompleterData(name = "arena.removekit")
    public List<String> arenaRemoveKitCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            this.plugin.getService(ArenaService.class).getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @CommandData(
            name = "arena.removekit",
            isAdminOnly = true,
            usage = "arena removekit <arenaName> <kitName>",
            description = "Remover um kit de uma arena"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            command.sendUsage();
            return;
        }

        String arenaName = args[0];
        String kitName = args[1];

        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        Arena arena = arenaService.getArenaByName(arenaName);
        if (arena.getName() == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_NOT_FOUND).replace("{arena-name}", arenaName));
            return;
        }

        if (this.plugin.getService(KitService.class).getKit(kitName).getName() == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.KIT_NOT_FOUND).replace("{kit-name}", kitName));
            return;
        }

        if (!arena.getKits().contains(kitName)) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_ARENA_DOES_NOT_HAVE_KIT)
                    .replace("{arena-name}", arenaName)
                    .replace("{kit-name}", kitName)
            );
            return;
        }

        arena.getKits().remove(kitName);
        arenaService.saveArena(arena);

        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_KIT_REMOVED)
                .replace("{arena-name}", arenaName)
                .replace("{kit-name}", kitName)
        );
    }
}