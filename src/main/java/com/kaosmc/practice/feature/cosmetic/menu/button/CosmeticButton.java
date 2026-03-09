package com.kaosmc.practice.feature.cosmetic.menu.button;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.data.types.ProfileCosmeticData;
import com.kaosmc.practice.feature.cosmetic.model.BaseCosmetic;
import com.kaosmc.practice.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Kaos
 * @date 6/23/2025
 */
@AllArgsConstructor
public class CosmeticButton extends Button {
    protected final KaosPractice plugin = KaosPractice.getInstance();
    private final BaseCosmetic cosmetic;

    @Override
    public ItemStack getButtonItem(Player player) {
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        boolean isSelected = profile.getProfileData().getCosmeticData().isSelected(cosmetic);
        boolean hasPermission = player.hasPermission(cosmetic.getPermission());

        List<String> lore = new ArrayList<>();
        lore.add(CC.MENU_BAR);
        lore.addAll(cosmetic.getDisplayLore());
        lore.add("");
        if (hasPermission) {
            lore.add(isSelected ? "&eSelecionado." : "&aClique para selecionar.");
        } else {
            lore.add("&cVocê não possui este cosmético.");
        }
        lore.add(CC.MENU_BAR);

        return new ItemBuilder(cosmetic.getIcon())
                .name("&6&l" + cosmetic.getName())
                .lore(lore)
                .glow(isSelected)
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType == ClickType.MIDDLE || clickType == ClickType.RIGHT || clickType == ClickType.NUMBER_KEY || clickType == ClickType.DROP || clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT) {
            return;
        }

        LocaleService localeService = KaosPractice.getInstance().getService(LocaleService.class);
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        ProfileCosmeticData cosmeticData = profile.getProfileData().getCosmeticData();

        if (cosmeticData.isSelected(cosmetic)) {
            player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.COSMETIC_ALREADY_SELECTED).replace("{cosmetic-name}", cosmetic.getName()));
            this.playFail(player);
            return;
        }

        if (!player.hasPermission(cosmetic.getPermission())) {
            player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.COSMETIC_NOT_OWNED).replace("{cosmetic-name}", cosmetic.getName()));
            this.playFail(player);
            return;
        }

        cosmetic.getType().handleSelection(cosmetic, player);

        cosmeticData.setSelected(cosmetic);

        this.playSuccess(player);
        player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.COSMETIC_SELECTED).replace("{cosmetic-name}", cosmetic.getName()));
    }
}
