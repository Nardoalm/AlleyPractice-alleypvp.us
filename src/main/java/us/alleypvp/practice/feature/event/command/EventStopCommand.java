package us.alleypvp.practice.feature.event.command;

import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.feature.event.EventService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

public class EventStopCommand extends BaseCommand {
    @CommandData(
            name = "event.stop",
            aliases = {"events.stop"},
            usage = "event stop",
            description = "Encerra o evento ativo."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        EventService eventService = this.plugin.getService(EventService.class);

        if (!eventService.canManageActiveEvent(player)) {
            player.sendMessage(CC.translate("&cVocê não pode encerrar o evento atual."));
            return;
        }

        eventService.stopActiveEvent("&cO evento foi encerrado por " + player.getName() + "&c.");
    }
}
