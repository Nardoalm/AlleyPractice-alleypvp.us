package com.kaosmc.practice.feature.layout.menu.button.editor;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.InventoryUtil;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.layout.LayoutService;
import com.kaosmc.practice.feature.layout.data.LayoutData;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.library.menu.Menu;
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
        if (this.kit == null || this.layout == null) {
            player.sendMessage(CC.translate("&cNao foi possivel salvar este layout agora."));
            return;
        }

        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService != null ? profileService.getProfile(player.getUniqueId()) : null;
        if (profile == null || profile.getProfileData() == null || profile.getProfileData().getLayoutData() == null) {
            player.sendMessage(CC.translate("&cNao foi possivel salvar este layout agora."));
            return;
        }

        LayoutData layout = profile.getProfileData().getLayoutData().getLayout(this.kit.getName(), this.layout.getName());
        if (layout == null) {
            player.sendMessage(CC.translate("&cLayout nao encontrado para este kit."));
            return;
        }

        layout.setDisplayName(this.layout.getDisplayName());
        layout.setItems(InventoryUtil.cloneItemStackArray(player.getInventory().getContents()));
        profile.save();

        LayoutService layoutService = KaosPractice.getInstance().getService(LayoutService.class);
        Menu layoutMenu = layoutService != null ? layoutService.getLayoutMenu() : null;
        if (layoutMenu == null) {
            player.sendMessage(CC.translate("&cNao foi possivel voltar ao menu de layout agora."));
            return;
        }

        layoutMenu.openMenu(player);
    }
}
