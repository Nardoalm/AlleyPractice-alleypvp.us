package us.alleypvp.practice.feature.arena.command.impl.data;

import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.feature.arena.Arena;
import us.alleypvp.practice.feature.arena.ArenaService;
import us.alleypvp.practice.feature.arena.ArenaType;
import us.alleypvp.practice.feature.arena.internal.types.StandAloneArena;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 06/03/2025
 */
public class ArenaSetHeightLimitCommand extends BaseCommand {
    @CommandData(
            name = "arena.setheightlimit",
            aliases = {"arena.limit", "arena.height"},
            isAdminOnly = true,
            usage = "arena setheightlimit <arenaName> <heightLimit>",
            description = "Define o limite de altura para uma arena standalone."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            command.sendUsage();
            return;
        }

        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        Arena arena = arenaService.getArenaByName(args[0]);
        if (arena == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_NOT_FOUND).replace("{arena-name}", args[0]));
            return;
        }

        if (arena.getType() != ArenaType.STANDALONE) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_MUST_BE_STANDALONE).replace("{arena-name}", arena.getName()));
            return;
        }

        int heightLimit;
        try {
            heightLimit = Integer.parseInt(args[1]);
            if (heightLimit < 0 || heightLimit > 256) {
                player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_HEIGHT_LIMIT_OUT_OF_BOUNDS));
                return;
            }
        } catch (NumberFormatException exception) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_NUMBER).replace("{input}", args[1]));
            return;
        }

        ((StandAloneArena) arena).setHeightLimit(heightLimit);
        arenaService.saveArena(arena);

        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_HEIGHT_LIMIT_SET)
                .replace("{arena-name}", arena.getName())
                .replace("{height-limit}", String.valueOf(heightLimit))
        );
    }
}