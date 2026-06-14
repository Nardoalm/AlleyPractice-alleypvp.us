package us.alleypvp.practice.feature.ffa.menu;

import us.alleypvp.practice.library.menu.Button;
import us.alleypvp.practice.feature.ffa.FFAMatch;
import us.alleypvp.practice.common.item.ItemBuilder;
import us.alleypvp.practice.common.SoundUtil;
import us.alleypvp.practice.common.text.CC;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 01:29
 */
@AllArgsConstructor
public class FFAButton extends Button {
    private final FFAMatch match;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.match.getKit().getIcon())
                .name("&b&l" + this.match.getKit().getMenuTitle())
                .durability(this.match.getKit().getDurability())
                .lore(
                        CC.MENU_BAR,
                        " &fJogando: &b" + this.match.getPlayers().size() + "/" + this.match.getMaxPlayers(),
                        " &fArena: &b" + this.match.getArena().getName(),
                        " &fKit: &b" + this.match.getKit().getName(),
                        "",
                        "&aClique para entrar.",
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
