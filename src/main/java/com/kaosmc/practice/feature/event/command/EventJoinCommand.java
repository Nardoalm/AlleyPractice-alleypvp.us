package com.kaosmc.practice.feature.event.command;

import com.kaosmc.practice.feature.event.EventService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;

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
