package us.alleypvp.practice.core.profile.menu.shop.button;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.common.item.ItemBuilder;
import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.core.locale.LocaleService;
import us.alleypvp.practice.core.locale.internal.impl.SettingsLocaleImpl;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.feature.cosmetic.model.BaseCosmetic;
import us.alleypvp.practice.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
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
            lore.add("&aVocê já possui este item.");
        } else {
            lore.add(" &fPreço: &b$" + cosmetic.getPrice());
            lore.add("");
            lore.add("&aClique para comprar.");
        }
        lore.add(CC.MENU_BAR);

        return new ItemBuilder(cosmetic.getIcon())
                .name("&b&l" + cosmetic.getName())
                .lore(lore)
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Profile profile = AlleyPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        LocaleService localeService = AlleyPractice.getInstance().getService(LocaleService.class);

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
