package com.kaosmc.practice.feature.layout.menu.button.editor;

import com.kaosmc.practice.common.constants.MessageConstant;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.feature.layout.data.LayoutData;
import com.kaosmc.practice.library.menu.Button;
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
public class LayoutRenameButton extends Button {
    private final LayoutData layout;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.NAME_TAG)
                .name("&6&lRename Layout")
                .lore(
                        CC.MENU_BAR,
                        "&7Change the display",
                        "&7name of the layout.",
                        "",
                        "&aClick to rename",
                        CC.MENU_BAR
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        player.sendMessage(MessageConstant.IN_DEVELOPMENT);
    }
}
