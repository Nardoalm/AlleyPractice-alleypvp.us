package com.kaosmc.practice.feature.match.snapshot.menu.button;

import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.common.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Emmy
 * @project Kaos
 * @since 27/06/2025
 */
@AllArgsConstructor
public class SnapshotDataButton extends Button {
    private final String name;
    private final List<String> lore;
    private final Material material;
    private final int durability;
    private final int amount;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.material)
                .name(this.name)
                .lore(this.lore)
                .durability(this.durability)
                .hideMeta()
                .amount(this.amount)
                .build();
    }
}