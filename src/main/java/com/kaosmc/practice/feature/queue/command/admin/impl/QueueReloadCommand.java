package com.kaosmc.practice.feature.queue.command.admin.impl;

import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.queue.QueueService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Kaos
 * @date 5/26/2024
 */
public class QueueReloadCommand extends BaseCommand {
    @CommandData(
            name = "queue.reload",
            aliases = {"reloadqueue", "reloadqueues"},
            isAdminOnly = true,
            usage = "/queue reload",
            description = "Reload all queues from configuration"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        this.plugin.getService(QueueService.class).reloadQueues();
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.QUEUE_RELOADED));
    }
}