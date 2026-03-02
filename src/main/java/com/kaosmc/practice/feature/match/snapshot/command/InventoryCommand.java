package com.kaosmc.practice.feature.match.snapshot.command;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.match.snapshot.Snapshot;
import com.kaosmc.practice.feature.match.snapshot.SnapshotService;
import com.kaosmc.practice.feature.match.snapshot.menu.SnapshotMenu;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Emmy
 * @project Kaos
 * @date 15/06/2024 - 22:19
 */
public class InventoryCommand extends BaseCommand {
    @CommandData(
            name = "inventory",
            aliases = {"inv", "snapshot"},
            usage = "inventory <playerId>",
            description = "View a player's inventory."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            command.sendUsage();
            return;
        }

        String uuid = args[0];
        Snapshot snapshot;
        SnapshotService snapshotRepository = KaosPractice.getInstance().getService(SnapshotService.class);

        try {
            snapshot = snapshotRepository.getSnapshot(UUID.fromString(uuid));
        } catch (Exception exception) {
            snapshot = snapshotRepository.getSnapshot(uuid);
        }

        if (snapshot == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.SNAPSHOT_INVENTORY_EXPIRED));
            return;
        }

        new SnapshotMenu(snapshot).openMenu(player);
    }
}