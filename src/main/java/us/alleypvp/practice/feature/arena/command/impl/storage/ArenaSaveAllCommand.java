package us.alleypvp.practice.feature.arena.command.impl.storage;

import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.feature.arena.Arena;
import us.alleypvp.practice.feature.arena.ArenaService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaSaveAllCommand extends BaseCommand {
    @CommandData(
            name = "arena.saveall",
            isAdminOnly = true,
            usage = "arena saveall",
            description = "Salva todas as arenas no armazenamento"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        for (Arena arena : this.plugin.getService(ArenaService.class).getArenas()) {
            this.plugin.getService(ArenaService.class).saveArena(arena);
        }

        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_SAVED_ALL));
    }
}
