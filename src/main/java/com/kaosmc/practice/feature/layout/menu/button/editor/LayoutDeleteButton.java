package com.kaosmc.practice.feature.layout.menu.button.editor;

import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.feature.layout.data.LayoutData;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Kaos
 * @since 03/05/2025
 */
@AllArgsConstructor
public class LayoutDeleteButton extends Button {
    private final LayoutData layout;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.INK_SACK)
                .name("&c&lDelete Layout")
                .durability(1)
                .lore(
                        CC.MENU_BAR,
                        "&7Warning: Permanent!",
                        "",
                        "&aClick to delete.",
                        CC.MENU_BAR
                )
                .hideMeta()
                .build();
    }
}
