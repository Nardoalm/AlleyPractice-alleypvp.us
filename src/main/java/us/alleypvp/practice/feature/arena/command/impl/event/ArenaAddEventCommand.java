package us.alleypvp.practice.feature.arena.command.impl.event;

import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.feature.arena.Arena;
import us.alleypvp.practice.feature.arena.ArenaService;
import us.alleypvp.practice.feature.arena.ArenaType;
import us.alleypvp.practice.feature.event.EventDefinition;
import us.alleypvp.practice.feature.event.EventService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import us.alleypvp.practice.library.command.annotation.CompleterData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ArenaAddEventCommand extends BaseCommand {
    @CompleterData(name = "arena.eventadd")
    public List<String> arenaAddEventCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            this.plugin.getService(ArenaService.class).getArenas().forEach(arena -> completion.add(arena.getName()));
        } else if (command.getArgs().length == 2 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            this.plugin.getService(EventService.class).getEventDefinitions().forEach(definition -> completion.add(definition.getKey()));
        }

        return completion;
    }

    @CommandData(
            name = "arena.eventadd",
            isAdminOnly = true,
            usage = "arena eventadd <arenaName> <eventKey>",
            description = "Adiciona um evento permitido a uma arena EVENT"
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
        String eventKey = args[1].toLowerCase();

        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        Arena arena = arenaService.getArenaByName(arenaName);
        if (arena == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_NOT_FOUND).replace("{arena-name}", arenaName));
            return;
        }

        if (arena.getType() != ArenaType.EVENT) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_MUST_BE_EVENT).replace("{arena-name}", arenaName));
            return;
        }

        EventDefinition definition = this.plugin.getService(EventService.class).getEventDefinition(eventKey);
        if (definition == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_TYPE)
                    .replace("{type}", "evento")
                    .replace("{types}", "sumo, brackets, gulag, lms, knockout, oitc, parkour, dropper, skywars, spleef, stoplight, corners, thimble, tnttag"));
            return;
        }

        if (arena.getEvents().contains(eventKey)) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_EVENT_ALREADY_ADDED)
                    .replace("{arena-name}", arenaName)
                    .replace("{event-name}", eventKey));
            return;
        }

        arena.getEvents().add(eventKey);
        arenaService.saveArena(arena);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_EVENT_ADDED)
                .replace("{arena-name}", arenaName)
                .replace("{event-name}", eventKey));
    }
}
