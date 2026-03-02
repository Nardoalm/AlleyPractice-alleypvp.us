package com.kaosmc.practice.feature.match.snapshot.menu.button;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.feature.match.snapshot.SnapshotService;
import com.kaosmc.practice.feature.match.snapshot.Snapshot;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Kaos
 * @date 07/10/2024
 */
@AllArgsConstructor
public class SnapshotOpponentButton extends Button {
    private final Snapshot snapshot;

    @Override
    public ItemStack getButtonItem(Player player) {
        Snapshot opponentSnapshot = KaosPractice.getInstance().getService(SnapshotService.class).getSnapshot(this.snapshot.getOpponent());
        if (opponentSnapshot == null) {
            return new ItemBuilder(Material.BARRIER)
                    .name(CC.translate("&cOpponent Not Found"))
                    .lore("&7The opponent's snapshot could not be found.")
                    .hideMeta()
                    .build();
        }

        return new ItemBuilder(Material.PAPER)
                .name(CC.translate("&6View Opponent"))
                .lore("&7Click to view &6" + opponentSnapshot.getUsername() + "'s &7inventory.")
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        player.performCommand("inventory " + this.snapshot.getOpponent());
    }
}