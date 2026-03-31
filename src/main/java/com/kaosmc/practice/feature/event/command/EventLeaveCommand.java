package com.kaosmc.practice.feature.event.command;

import com.kaosmc.practice.feature.event.EventService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;

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
