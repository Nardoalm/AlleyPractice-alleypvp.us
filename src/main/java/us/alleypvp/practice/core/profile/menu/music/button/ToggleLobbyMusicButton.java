package us.alleypvp.practice.core.profile.menu.music.button;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.library.menu.Button;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.common.item.ItemBuilder;
import us.alleypvp.practice.common.text.LoreHelper;
import us.alleypvp.practice.common.text.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project kaos-practice
 * @since 20/07/2025
 */
public class ToggleLobbyMusicButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = AlleyPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        return new ItemBuilder(Material.EMERALD)
                .name("&b&lMúsica do Lobby")
                .lore(
                        CC.MENU_BAR,
                        LoreHelper.displayEnabled(profile.getProfileData().getSettingData().isLobbyMusicEnabled()),
                        "",
                        "&aClique para alternar.",
                        CC.MENU_BAR
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        player.performCommand("togglelobbymusic");

        this.playNeutral(player);
    }
}
