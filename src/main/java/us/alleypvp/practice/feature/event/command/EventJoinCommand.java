package us.alleypvp.practice.feature.event.command;

import us.alleypvp.practice.feature.event.EventService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;

public class EventJoinCommand extends BaseCommand {
    @CommandData(
            name = "event.join",
            aliases = {"events.join"},
            usage = "event join",
            description = "Entra no evento ativo."
    )
    @Override
    public void onCommand(CommandArgs command) {
        this.plugin.getService(EventService.class).joinActiveEvent(command.getPlayer());
    }
}
