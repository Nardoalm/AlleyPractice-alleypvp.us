package com.kaosmc.practice.feature.ffa.menu;

import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.feature.ffa.FFAMatch;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.SoundUtil;
import com.kaosmc.practice.common.text.CC;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Kaos
 * @date 23/05/2024 - 01:29
 */
@AllArgsConstructor
public class FFAButton extends Button {
    private final FFAMatch match;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.match.getKit().getIcon())
                .name("&6&l" + this.match.getKit().getMenuTitle())
                .durability(this.match.getKit().getDurability())
                .lore(
                        CC.MENU_BAR,
                        " &fPlaying: &6" + this.match.getPlayers().size() + "/" + this.match.getMaxPlayers(),
                        " &fArena: &6" + this.match.getArena().getName(),
                        " &fKit: &6" + this.match.getKit().getName(),
                        "",
                        "&aClick to join.",
                        CC.MENU_BAR

                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        if (clickType != ClickType.LEFT) return;
        SoundUtil.playSuccess(player);
        this.match.join(player);
    }
}