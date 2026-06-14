package us.alleypvp.practice.core.profile.menu.statistic.button;

import us.alleypvp.practice.library.menu.Button;
import us.alleypvp.practice.feature.division.menu.DivisionsMenu;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.common.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @since 21/04/2025
 */
@AllArgsConstructor
public class DivisionViewButton extends Button {

    //TODO: when implementing global levels, profile field is gonna be required to get the level and so on...

    private final Profile profile;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.DIAMOND)
                .name("&b&lDivisões")
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
