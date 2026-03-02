package com.kaosmc.practice.feature.layout.menu.button;

import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.feature.kit.KitCategory;
import com.kaosmc.practice.feature.layout.menu.LayoutMenu;
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
 * @since 03/05/2025
 */
@AllArgsConstructor
public class LayoutModeSwitcherButton extends Button {
    private KitCategory kitCategory;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.ARROW)
                .name("&6&l" + this.kitCategory.getName() + " Modes")
                .lore(
                        CC.MENU_BAR,
                        ("&f " + this.kitCategory.getDescription()),
                        "",
                        "&aClick to view!",
                        CC.MENU_BAR
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        if (this.kitCategory == KitCategory.EXTRA) {
            new LayoutMenu(KitCategory.EXTRA).openMenu(player);
            return;
        }

        new LayoutMenu(KitCategory.NORMAL).openMenu(player);
    }
}