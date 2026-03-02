package com.kaosmc.practice.feature.arena.command.impl.storage;

import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.arena.Arena;
import com.kaosmc.practice.feature.arena.ArenaService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Kaos
 * @date 5/20/2024
 */
public class ArenaSaveAllCommand extends BaseCommand {
    @CommandData(
            name = "arena.saveall",
            isAdminOnly = true,
            usage = "arena saveall",
            description = "Saves all arenas to storage"
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
