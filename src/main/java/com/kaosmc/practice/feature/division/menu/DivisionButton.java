package com.kaosmc.practice.feature.division.menu;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.feature.division.Division;
import com.kaosmc.practice.feature.title.menu.TitleMenu;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Kaos
 * @date 25/01/2025
 */
@Getter
@AllArgsConstructor
public class DivisionButton extends Button {
    private final Division division;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.division.getIcon())
                .durability(this.division.getDurability())
                .name("&6&lDivisão " + this.division.getDisplayName())
                .lore(
                        CC.MENU_BAR,
                        "&f&l● &6Tiers: &f" + this.division.getTiers().size(),
                        "  &7▶ (" + this.division.getTiers().get(0).getRequiredWins() + " - " + this.division.getTotalWins() + " vitórias)",
                        "",
                        " &fPara cada kit, você terá",
                        " &fuma divisão com base nas",
                        " &6vitórias unranked&f.",
                        "",
                        "&aClique para ver seus títulos.",
                        CC.MENU_BAR
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        new TitleMenu(KaosPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId())).openMenu(player);
    }
}
