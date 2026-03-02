package com.kaosmc.practice.core.profile.menu.shop.button;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.SettingsLocaleImpl;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.feature.cosmetic.model.BaseCosmetic;
import com.kaosmc.practice.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
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
public class ShopItemButton extends Button {
    private final BaseCosmetic cosmetic;

    @Override
    public ItemStack getButtonItem(Player player) {
        boolean hasPermission = player.hasPermission(cosmetic.getPermission());

        List<String> lore = new ArrayList<>();
        lore.add(CC.MENU_BAR);
        lore.addAll(cosmetic.getDisplayLore());
        lore.add("");

        if (hasPermission) {
            lore.add("&aYou already own this item.");
        } else {
            lore.add(" &fPrice: &6$" + cosmetic.getPrice());
            lore.add("");
            lore.add("&aClick to purchase.");
        }
        lore.add(CC.MENU_BAR);

        return new ItemBuilder(cosmetic.getIcon())
                .name("&6&l" + cosmetic.getName())
                .lore(lore)
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Profile profile = KaosPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        LocaleService localeService = KaosPractice.getInstance().getService(LocaleService.class);

        if (player.hasPermission(cosmetic.getPermission())) {
            player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.COSMETIC_ALREADY_OWNED));
            this.playFail(player);
            return;
        }

        if (profile.getProfileData().getCoins() < cosmetic.getPrice()) {
            player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.COSMETIC_PURCHASE_INSUFFICIENT_FUNDS));
            this.playFail(player);
            return;
        }

        profile.getProfileData().setCoins(profile.getProfileData().getCoins() - cosmetic.getPrice());

        String command = localeService.getString(SettingsLocaleImpl.GRANT_COSMETIC_PERMISSION_COMMAND)
                .replace("{player}", player.getName())
                .replace("{permission}", cosmetic.getPermission());

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);

        player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.COSMETIC_PURCHASE_SUCCESS).replace("{cosmetic}", cosmetic.getName()));
        this.playSuccess(player);
    }
}