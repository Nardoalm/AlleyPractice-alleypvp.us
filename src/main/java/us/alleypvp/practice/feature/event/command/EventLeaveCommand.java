package us.alleypvp.practice.feature.event.command;

import us.alleypvp.practice.feature.event.EventService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;

public class EventLeaveCommand extends BaseCommand {
    @CommandData(
            name = "event.leave",
            aliases = {"events.leave"},
            usage = "event leave",
            description = "Sai do evento atual."
    )
    @Override
    public void onCommand(CommandArgs command) {
        this.plugin.getService(EventService.class).leaveActiveEvent(command.getPlayer(), false);
    }
}
