package com.kaosmc.practice.feature.match.snapshot.menu.button.items;

import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.feature.match.snapshot.Snapshot;
import com.kaosmc.practice.common.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Kaos
 * @date 07/10/2024
 */
@AllArgsConstructor
public class SnapshotArmorButton extends Button {
    private final Snapshot snapshot;
    private int armorPart;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.snapshot.getArmor()[this.armorPart]).build();
    }
}