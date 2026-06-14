package us.alleypvp.practice.feature.arena.command.impl.data;

import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.feature.arena.Arena;
import us.alleypvp.practice.feature.arena.ArenaService;
import us.alleypvp.practice.feature.arena.ArenaType;
import us.alleypvp.practice.feature.arena.ArenaValidator;
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
public class ArenaToggleCommand extends BaseCommand {
    @CompleterData(name = "arena.toggle")
    public List<String> arenaToggleCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            this.plugin.getService(ArenaService.class).getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @CommandData(
            name = "arena.toggle",
            isAdminOnly = true,
            usage = "arena toggle <arenaName>",
            description = "Alterna o status habilitado de uma arena"
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

        if (arena.getType() == ArenaType.FFA) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_CANNOT_TOGGLE_FFA));
            return;
        }

        ArenaValidator validator = arenaService.getArenaValidator();
        if (!validator.isEligible(player, arena)) {
            return;
        }

        arena.setEnabled(!arena.isEnabled());
        arenaService.saveArena(arena);

        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_TOGGLED)
                .replace("{arena-name}", arena.getName())
                .replace("{status}", arena.isEnabled() ? "ativada" : "desativada")
        );
    }
}
