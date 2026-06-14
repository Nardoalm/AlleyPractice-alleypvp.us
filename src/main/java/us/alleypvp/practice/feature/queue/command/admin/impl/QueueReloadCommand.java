package us.alleypvp.practice.feature.queue.command.admin.impl;

import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.feature.queue.QueueService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class QueueReloadCommand extends BaseCommand {
    @CommandData(
            name = "queue.reload",
            aliases = {"reloadqueue", "reloadqueues"},
            isAdminOnly = true,
            usage = "/queue reload",
            description = "Recarrega todas as filas da configuração"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        this.plugin.getService(QueueService.class).reloadQueues();
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.QUEUE_RELOADED));
    }
}