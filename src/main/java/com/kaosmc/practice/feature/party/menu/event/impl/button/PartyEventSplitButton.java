package com.kaosmc.practice.feature.party.menu.event.impl.button;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.SettingsLocaleImpl;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.feature.arena.Arena;
import com.kaosmc.practice.feature.arena.ArenaService;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.party.Party;
import com.kaosmc.practice.feature.party.PartyService;
import com.kaosmc.practice.feature.party.menu.event.impl.PartyEventSplitArenaSelectorMenu;
import com.kaosmc.practice.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Kaos
 * @since 16/06/2025
 */
@AllArgsConstructor
public class PartyEventSplitButton extends Button {
    protected final KaosPractice plugin = KaosPractice.getInstance();
    private final Kit kit;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.kit.getIcon())
                .name("&6&l" + this.kit.getDisplayName())
                .lore(
                        CC.MENU_BAR,
                        "&aClick to select!",
                        CC.MENU_BAR
                )
                .durability(this.kit.getDurability())
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        if (player.hasPermission(this.plugin.getService(LocaleService.class).getString(SettingsLocaleImpl.PERMISSION_DONATOR_PARTY_ARENA_SELECTOR))) {
            new PartyEventSplitArenaSelectorMenu(this.kit).openMenu(player);
            return;
        }

        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService != null ? profileService.getProfile(player.getUniqueId()) : null;
        Party party = profile != null ? profile.getParty() : null;
        if (party == null) {
            player.closeInventory();
            player.sendMessage(KaosPractice.getInstance().getService(LocaleService.class).getString(GlobalMessagesLocaleImpl.ERROR_YOU_NOT_IN_PARTY));
            return;
        }

        Arena arena = KaosPractice.getInstance().getService(ArenaService.class).getRandomArena(this.kit);
        if (arena == null) {
            player.closeInventory();
            player.sendMessage(CC.translate("&cNenhuma arena disponível para este kit no momento."));
            return;
        }
        KaosPractice.getInstance().getService(PartyService.class).startMatch(this.kit, arena, party);
    }
}
