package com.kaosmc.practice.feature.layout.menu.button.editor;

import com.kaosmc.practice.common.InventoryUtil;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.feature.kit.Kit;
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
public class LayoutResetItemsButton extends Button {
    private final Kit kit;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.WOOL)
                .name("&6&lResetar Itens")
                .durability(4)
                .lore(
                        CC.MENU_BAR,
                        "&7Reseta os itens para o padrão.",
                        "",
                        "&aClique para resetar.",
                        CC.MENU_BAR
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;
        if (this.kit == null) {
            player.sendMessage(CC.translate("&cNao foi possivel resetar os itens deste layout."));
            return;
        }

        player.getInventory().clear();
        player.getInventory().setContents(InventoryUtil.getEditableKitItems(this.kit));
        player.updateInventory();
    }
}
