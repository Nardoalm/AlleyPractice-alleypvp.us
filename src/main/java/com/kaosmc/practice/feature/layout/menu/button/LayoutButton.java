package com.kaosmc.practice.feature.layout.menu.button;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.InventoryUtil;
import com.kaosmc.practice.common.constants.MessageConstant;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.common.text.Symbol;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.enums.ProfileState;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.kit.setting.types.mode.KitSettingRaiding;
import com.kaosmc.practice.feature.layout.data.LayoutData;
import com.kaosmc.practice.feature.layout.menu.LayoutEditorMenu;
import com.kaosmc.practice.feature.layout.menu.LayoutSelectRoleKitMenu;
import com.kaosmc.practice.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

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
        if (this.kit == null) {
            return new ItemBuilder(Material.BARRIER)
                    .name("&cKit indisponivel")
                    .lore(CC.MENU_BAR, "&7Nao foi possivel carregar este kit.", CC.MENU_BAR)
                    .hideMeta()
                    .build();
        }

        return new ItemBuilder(this.kit.getIcon())
                .name("&6&l" + this.kit.getDisplayName())
                .durability(this.kit.getDurability())
                .lore(
                        CC.MENU_BAR,
                        "&7Shift-Clique: &c(Não implementado)",
                        " &7" + Symbol.SINGULAR_ARROW_R_2 + " Mais layouts",
                        "",
                        "&aClique para editar.",
                        CC.MENU_BAR
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService != null ? profileService.getProfile(player.getUniqueId()) : null;
        if (profile == null || this.kit == null) {
            player.sendMessage(CC.translate("&cNao foi possivel abrir este layout agora."));
            return;
        }

        if (profile.getState() == ProfileState.WAITING) {
            player.sendMessage(CC.translate("&cSaia da fila para editar o layout."));
            return;
        }

        if (clickType == ClickType.LEFT) {
            if (this.kit.isSettingEnabled(KitSettingRaiding.class)) {
                new LayoutSelectRoleKitMenu(this.kit).openMenu(player);
                return;
            }

            LayoutData layout = this.getOrCreateFirstLayout(profile, this.kit);
            if (layout == null) {
                player.sendMessage(CC.translate("&cNenhum layout encontrado para este kit."));
                return;
            }
            new LayoutEditorMenu(this.kit, layout).openMenu(player);
        } else if (clickType == ClickType.SHIFT_LEFT) {
            player.sendMessage(MessageConstant.IN_DEVELOPMENT);
        }
    }

    private LayoutData getOrCreateFirstLayout(Profile profile, Kit kit) {
        if (profile == null
                || profile.getProfileData() == null
                || profile.getProfileData().getLayoutData() == null
                || profile.getProfileData().getLayoutData().getLayouts() == null
                || kit == null
                || kit.getName() == null) {
            return null;
        }

        List<LayoutData> layouts = profile.getProfileData().getLayoutData().getLayouts().get(kit.getName());
        if (layouts == null || layouts.isEmpty()) {
            profile.getProfileData().getLayoutData().addLayout(
                    kit.getName(),
                    "Layout1",
                    "Modelo 1",
                    InventoryUtil.getEditableKitItems(kit)
            );
            layouts = profile.getProfileData().getLayoutData().getLayouts().get(kit.getName());
        }

        return layouts.stream().filter(Objects::nonNull).findFirst().orElse(null);
    }
}
