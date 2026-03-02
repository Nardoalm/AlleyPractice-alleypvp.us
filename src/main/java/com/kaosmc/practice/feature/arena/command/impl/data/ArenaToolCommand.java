package com.kaosmc.practice.feature.arena.command.impl.data;

import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.arena.selection.ArenaSelection;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Kaos
 * @date 5/20/2024
 */
public class ArenaToolCommand extends BaseCommand {
    @CommandData(
            name = "arena.tool",
            aliases = "arena.wand",
            isAdminOnly = true,
            usage = "arena tool",
            description = "Gives or removes the arena selection tool"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (player.getInventory().first(ArenaSelection.SELECTION_TOOL) != -1) {
            player.getInventory().remove(ArenaSelection.SELECTION_TOOL);
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_SELECTION_TOOL_REMOVED));
            player.updateInventory();
            return;
        }

        player.getInventory().addItem(ArenaSelection.SELECTION_TOOL);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_SELECTION_TOOL_ADDED));
        player.updateInventory();
    }
}