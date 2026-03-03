package com.kaosmc.practice.feature.arena.command.impl.storage;

import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.arena.Arena;
import com.kaosmc.practice.feature.arena.ArenaService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project kaos-practice
 * @since 09/09/2025
 */
public class ArenaSaveCommand extends BaseCommand {
    @CommandData(
            name = "arena.save",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "arena save <name>",
            description = "Salva uma arena no banco de dados."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        String arenaName = args[0];
        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        Arena arena = arenaService.getArenaByName(arenaName);
        if (arena == null) {
            sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_NOT_FOUND).replace("{arena-name}", arenaName));
            return;
        }

        arenaService.saveArena(arena);
        sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_SAVED).replace("{arena-name}", arena.getName()));
    }
}