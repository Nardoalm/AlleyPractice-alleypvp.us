package us.alleypvp.practice.feature.arena.command.impl.data;

import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.feature.arena.Arena;
import us.alleypvp.practice.feature.arena.ArenaService;
import us.alleypvp.practice.feature.arena.ArenaType;
import us.alleypvp.practice.feature.arena.selection.ArenaSelection;
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
public class ArenaSetCuboidCommand extends BaseCommand {
    @CompleterData(name = "arena.setcuboid")
    public List<String> arenaCuboidCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            this.plugin.getService(ArenaService.class).getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @CommandData(
            name = "arena.setcuboid",
            isAdminOnly = true,
            usage = "arena setcuboid <arenaName>",
            description = "Define a seleção de cuboide para uma arena"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        ArenaSelection arenaSelection = ArenaSelection.createSelection(player);
        if (!arenaSelection.hasSelection()) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_NO_SELECTION));
            return;
        }

        String arenaName = args[0];
        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        Arena arena = arenaService.getArenaByName(arenaName);
        if (arena == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_NOT_FOUND).replace("{arena-name}", arenaName));
            return;
        }

        if (arena.getType() == ArenaType.FFA) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_CAN_NOT_SET_CUBOID_FFA));
            return;
        }

        arena.setMinimum(arenaSelection.getMinimum());
        arena.setMaximum(arenaSelection.getMaximum());
        arenaService.saveArena(arena);

        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_CUBOID_SET).replace("{arena-name}", arena.getName()));
    }
}