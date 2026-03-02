package com.kaosmc.practice.feature.layout.menu.button;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.constants.MessageConstant;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.common.text.Symbol;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.kit.setting.types.mode.KitSettingRaiding;
import com.kaosmc.practice.feature.layout.data.LayoutData;
import com.kaosmc.practice.feature.layout.menu.LayoutEditorMenu;
import com.kaosmc.practice.feature.layout.menu.LayoutSelectRoleKitMenu;
import com.kaosmc.practice.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Kaos
 * @since 03/05/2025
 */
@AllArgsConstructor
public class LayoutButton extends Button {
    private final Kit kit;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.kit.getIcon())
                .name("&6&l" + this.kit.getDisplayName())
                .durability(this.kit.getDurability())
                .lore(
                        CC.MENU_BAR,
                        "&7Shift-Click: &c(Not implemented)",
                        " &7" + Symbol.SINGULAR_ARROW_R_2 + " More Layouts",
                        "",
                        "&aClick to edit.",
                        CC.MENU_BAR
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Profile profile = KaosPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());

        if (clickType == ClickType.LEFT) {
            if (this.kit.isSettingEnabled(KitSettingRaiding.class)) {
                new LayoutSelectRoleKitMenu(this.kit).openMenu(player);
                return;
            }

            LayoutData layout = profile.getProfileData().getLayoutData().getLayouts().get(this.kit.getName()).get(0);
            new LayoutEditorMenu(this.kit, layout).openMenu(player);
        } else if (clickType == ClickType.SHIFT_LEFT) {
            player.sendMessage(MessageConstant.IN_DEVELOPMENT);
        }
    }
}