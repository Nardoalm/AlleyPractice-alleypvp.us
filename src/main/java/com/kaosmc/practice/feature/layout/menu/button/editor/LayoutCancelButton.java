package com.kaosmc.practice.feature.layout.menu.button.editor;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.feature.layout.LayoutService;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.library.menu.Menu;
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
public class LayoutCancelButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.WOOL)
                .name("&6&lCancelar")
                .durability(14)
                .lore(
                        CC.MENU_BAR,
                        "&7Cancela as alterações e",
                        "&7volta ao menu principal.",
                        "",
                        "&aClique para cancelar.",
                        CC.MENU_BAR
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        LayoutService layoutService = KaosPractice.getInstance().getService(LayoutService.class);
        Menu layoutMenu = layoutService != null ? layoutService.getLayoutMenu() : null;
        if (layoutMenu == null) {
            player.sendMessage(CC.translate("&cNao foi possivel voltar ao menu de layout agora."));
            return;
        }

        layoutMenu.openMenu(player);
    }
}
