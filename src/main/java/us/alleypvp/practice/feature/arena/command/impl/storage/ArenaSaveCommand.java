package us.alleypvp.practice.feature.arena.command.impl.storage;

import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.feature.arena.Arena;
import us.alleypvp.practice.feature.arena.ArenaService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
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