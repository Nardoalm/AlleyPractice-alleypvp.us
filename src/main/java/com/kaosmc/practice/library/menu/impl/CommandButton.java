package com.kaosmc.practice.library.menu.impl;

import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.common.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Emmy
 * @project Kaos
 * @date 15/06/2024 - 23:57
 */
@AllArgsConstructor
public class CommandButton extends Button {
    private String command;
    private ItemStack itemStack;
    private List<String> lore;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.itemStack)
                .lore(this.lore)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        player.performCommand(this.command);
    }
}