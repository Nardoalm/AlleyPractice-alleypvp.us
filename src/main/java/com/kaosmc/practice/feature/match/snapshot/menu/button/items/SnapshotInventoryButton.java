package com.kaosmc.practice.feature.match.snapshot.menu.button.items;

import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.common.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Kaos
 * @since 27/06/2025
 */
@AllArgsConstructor
public class SnapshotInventoryButton extends Button {
    private final ItemStack item;

    @Override
    public ItemStack getButtonItem(Player player) {
        if (this.item == null) {
            return new ItemStack(Material.AIR);
        }
        return new ItemBuilder(this.item).build();
    }
}
