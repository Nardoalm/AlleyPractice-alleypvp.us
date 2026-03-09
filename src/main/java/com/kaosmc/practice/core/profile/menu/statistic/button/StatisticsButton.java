package com.kaosmc.practice.core.profile.menu.statistic.button;

import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.core.profile.menu.statistic.StatisticsMenu;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Kaos
 * @date 5/26/2024
 */
public class StatisticsButton extends Button {

    /**
     * Gets the item to display in the menu.
     *
     * @param player the player viewing the menu
     * @return the item to display
     */
    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.PAPER)
                .name("&6&lSuas Estatísticas")
                .lore(
                        CC.MENU_BAR,
                        "&7Suas estatísticas são exibidas aqui.",
                        "&7Você pode ver vitórias, derrotas e mais.",
                        "",
                        "&aClique para ver suas estatísticas.",
                        CC.MENU_BAR
                )
                .build();
    }

    /**
     * Handles the click event for the button.
     *
     * @param player    the player who clicked the button
     * @param clickType the type of click
     */
    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        this.playNeutral(player);
        new StatisticsMenu(player).openMenu(player);
    }
}
