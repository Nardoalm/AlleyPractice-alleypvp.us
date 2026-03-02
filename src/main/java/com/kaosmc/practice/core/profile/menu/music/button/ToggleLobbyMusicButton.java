package com.kaosmc.practice.core.profile.menu.music.button;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.LoreHelper;
import com.kaosmc.practice.common.text.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project alley-practice
 * @since 20/07/2025
 */
public class ToggleLobbyMusicButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = KaosPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        return new ItemBuilder(Material.EMERALD)
                .name("&6&lLobby Music")
                .lore(
                        CC.MENU_BAR,
                        LoreHelper.displayEnabled(profile.getProfileData().getSettingData().isLobbyMusicEnabled()),
                        "",
                        "&aClick to toggle.",
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
