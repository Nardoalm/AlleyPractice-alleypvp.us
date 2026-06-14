package us.alleypvp.practice.feature.division.menu;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.library.menu.Button;
import us.alleypvp.practice.feature.division.Division;
import us.alleypvp.practice.feature.title.menu.TitleMenu;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.common.item.ItemBuilder;
import us.alleypvp.practice.common.text.CC;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
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
                .name("&b&lDivisão " + this.division.getDisplayName())
                .lore(
                        CC.MENU_BAR,
                        "&f&l● &bTiers: &f" + this.division.getTiers().size(),
                        "  &7▶ (" + this.division.getTiers().get(0).getRequiredWins() + " - " + this.division.getTotalWins() + " vitórias)",
                        "",
                        " &fPara cada kit, você terá",
                        " &fuma divisão com base nas",
                        " &bvitórias unranked&f.",
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

        new TitleMenu(AlleyPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId())).openMenu(player);
    }
}
