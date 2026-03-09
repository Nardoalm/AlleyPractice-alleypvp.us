package com.kaosmc.practice.library.menu.pagination.impl.button;

import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.library.menu.MenuUtil;
import com.kaosmc.practice.library.menu.pagination.PaginatedMenu;
import com.kaosmc.practice.library.menu.pagination.impl.menu.ViewAllPagesMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Kaos
 * @since 24/01/2025
 */
@AllArgsConstructor
public class PageButton extends Button {
    private PaginatedMenu menu;
    private int offset;

    @Override
    public ItemStack getButtonItem(Player player) {
        if (this.offset > 0) {
            if (MenuUtil.hasNext(player, offset, this.menu)) {
                return new ItemBuilder(Material.MELON)
                        .name("&6&lPróxima Página &7" + (this.menu.getPage() + "/" + this.menu.getPages(player)))
                        .lore(
                                CC.MENU_BAR,
                                "&7Clique direito:",
                                " &7▶ Ver todas",
                                "",
                                "&aClique para visualizar.",
                                CC.MENU_BAR
                        )
                        .hideMeta()
                        .build();
            } else {
                return new ItemBuilder(Material.MELON)
                        .name(CC.translate("&c&lPróxima Página"))
                        .lore(
                                CC.MENU_BAR,
                                " &cNão existe",
                                " &cpróxima página.",
                                CC.MENU_BAR
                        )
                        .hideMeta()
                        .build();
            }
        } else {
            if (MenuUtil.hasPrevious(offset, this.menu)) {
                return new ItemBuilder(Material.SPECKLED_MELON)
                        .name("&6&lPágina Anterior &7" + (this.menu.getPage() + "/" + this.menu.getPages(player)))
                        .lore(
                                CC.MENU_BAR,
                                "&7Clique direito:",
                                " &7▶ Ver todas",
                                "",
                                "&aClique para visualizar.",
                                CC.MENU_BAR
                        )
                        .hideMeta()
                        .build();
            } else {
                return new ItemBuilder(Material.SPECKLED_MELON)
                        .name(CC.translate("&c&lPágina Anterior"))
                        .lore(
                                CC.MENU_BAR,
                                " &cNão existe",
                                " &cpágina anterior.",
                                CC.MENU_BAR
                        )
                        .hideMeta()
                        .build();
            }
        }
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType == ClickType.RIGHT) {
            new ViewAllPagesMenu(this.menu).openMenu(player);
        } else if (clickType == ClickType.LEFT) {
            if (this.offset > 0) {
                if (MenuUtil.hasNext(player, offset, this.menu)) {
                    this.menu.modPage(player, this.offset);
                    this.playNeutral(player);
                } else {
                    this.playFail(player);
                }
            } else {
                if (MenuUtil.hasPrevious(offset, this.menu)) {
                    this.menu.modPage(player, this.offset);
                    this.playNeutral(player);
                } else {
                    this.playFail(player);
                }
            }
        }
    }
}
