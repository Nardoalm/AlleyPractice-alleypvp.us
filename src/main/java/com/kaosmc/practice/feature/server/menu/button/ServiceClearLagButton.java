package com.kaosmc.practice.feature.server.menu.button;

import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.common.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Kaos
 * @since 09/03/2025
 */
public class ServiceClearLagButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.FEATHER)
                .name("&c&lLimpar Lag")
                .lore(
                        "&fIsso executará a limpeza",
                        "&fde lag para o servidor.",
                        "",
                        "&cClique para limpar!"
                )
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        // Clear lag code here
    }
}
