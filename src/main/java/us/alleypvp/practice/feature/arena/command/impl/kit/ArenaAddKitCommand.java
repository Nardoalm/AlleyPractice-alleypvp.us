package us.alleypvp.practice.feature.arena.command.impl.kit;

import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.feature.arena.Arena;
import us.alleypvp.practice.feature.arena.ArenaService;
import us.alleypvp.practice.feature.arena.ArenaType;
import us.alleypvp.practice.feature.kit.Kit;
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
public class ArenaAddKitCommand extends BaseCommand {
    @CompleterData(name = "arena.addkit")
    public List<String> arenaAddKitCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            this.plugin.getService(ArenaService.class).getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @CommandData(
            name = "arena.addkit",
            isAdminOnly = true,
            usage = "arena addkit <arenaName> <kitName>",
            description = "Adiciona um kit a uma arena"
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
        if (arena == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_NOT_FOUND).replace("{arena-name}", arenaName));
            return;
        }

        if (arena.getType() == ArenaType.FFA || arena.getType() == ArenaType.EVENT) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_CANNOT_ADD_KITS_TO_FFA));
            return;
        }

        Kit kit = this.plugin.getService(KitService.class).getKit(kitName);
        if (kit == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.KIT_NOT_FOUND).replace("{kit-name}", kitName));
            return;
        }

        if (arena.getKits().contains(kit.getName())) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_KIT_ALREADY_ADDED)
                    .replace("{arena-name}", arenaName)
                    .replace("{kit-name}", kitName)
            );
            return;
        }

        arena.getKits().add(kit.getName());
        arenaService.saveArena(arena);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_KIT_ADDED)
                .replace("{arena-name}", arenaName)
                .replace("{kit-name}", kitName)
        );
    }
}
