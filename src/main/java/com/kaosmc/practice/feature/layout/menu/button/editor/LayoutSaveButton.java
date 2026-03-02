package com.kaosmc.practice.feature.layout.menu.button.editor;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.layout.LayoutService;
import com.kaosmc.practice.feature.layout.data.LayoutData;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
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
 * @since 03/05/2025
 */
@AllArgsConstructor
public class LayoutSaveButton extends Button {
    private final Kit kit;
    private final LayoutData layout;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.WOOL)
                .name("&6&lSave")
                .durability(13)
                .lore(
                        CC.MENU_BAR,
                        "&7Save changes &",
                        "&7return to main menu.",
                        "",
                        "&aClick to save.",
                        CC.MENU_BAR
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        Profile profile = KaosPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());

        LayoutData layout = profile.getProfileData().getLayoutData().getLayout(this.kit.getName(), this.layout.getName());
        layout.setDisplayName(this.layout.getDisplayName());
        layout.setItems(player.getInventory().getContents());

        KaosPractice.getInstance().getService(LayoutService.class).getLayoutMenu().openMenu(player);
    }
}