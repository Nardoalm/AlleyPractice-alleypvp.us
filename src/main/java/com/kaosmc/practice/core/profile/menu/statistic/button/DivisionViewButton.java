package com.kaosmc.practice.core.profile.menu.statistic.button;

import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.feature.division.menu.DivisionsMenu;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.common.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Kaos
 * @since 21/04/2025
 */
@AllArgsConstructor
public class DivisionViewButton extends Button {

    //TODO: when implementing global levels, profile field is gonna be required to get the level and so on...

    private final Profile profile;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.DIAMOND)
                .name("&6&lDivisões")
                .lore(
                        "&aClique para ver seu progresso de divisão."
                )
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        new DivisionsMenu().openMenu(player);
    }
}
