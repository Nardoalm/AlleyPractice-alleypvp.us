package com.kaosmc.practice.library.menu.impl;

import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.library.menu.Menu;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class BackButton extends Button {
    private Menu back;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.ARROW)
                .name("&c&lVoltar")
                .durability(0)
                .lore(
                        CC.MENU_BAR,
                        " &cClique aqui para voltar",
                        " &cao menu anterior.",
                        CC.MENU_BAR
                )
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        this.playNeutral(player);
        this.back.openMenu(player);
    }
}
