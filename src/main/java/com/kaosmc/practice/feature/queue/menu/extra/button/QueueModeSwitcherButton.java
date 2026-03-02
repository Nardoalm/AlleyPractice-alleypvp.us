package com.kaosmc.practice.feature.queue.menu.extra.button;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.feature.kit.KitCategory;
import com.kaosmc.practice.feature.queue.QueueService;
import com.kaosmc.practice.feature.queue.QueueType;
import com.kaosmc.practice.feature.queue.menu.extra.ExtraModesMenu;
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
 * @since 01/05/2025
 */
@AllArgsConstructor
public class QueueModeSwitcherButton extends Button {
    private final QueueType queueType;
    private final KitCategory kitCategory;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.ARROW)
                .name("&6&l" + this.kitCategory.getName() + " Modes")
                .lore(
                        CC.MENU_BAR,
                        ("&f " + this.kitCategory.getDescription()),
                        "",
                        "&aClick to view.",
                        CC.MENU_BAR
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        if (this.kitCategory == KitCategory.EXTRA) {
            new ExtraModesMenu(this.queueType).openMenu(player);
            return;
        }

        KaosPractice.getInstance().getService(QueueService.class).getQueueMenu().openMenu(player);
    }
}