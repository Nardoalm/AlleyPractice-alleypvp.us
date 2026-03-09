package com.kaosmc.practice.feature.party.menu.event.impl.button;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.feature.arena.Arena;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.party.Party;
import com.kaosmc.practice.feature.party.PartyService;
import com.kaosmc.practice.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Kaos
 * @since 16/06/2025
 */
@AllArgsConstructor
public class PartyEventSplitArenaSelectorButton extends Button {
    protected final KaosPractice plugin = KaosPractice.getInstance();
    private final Kit kit;
    private final Arena arena;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.PAPER)
                .name("&6&l" + this.arena.getName())
                .lore(
                        " &f◆ &6Kit: &f" + this.kit.getDisplayName(),
                        "",
                        "&aClique para selecionar!"
                )
                .durability(0)
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService != null ? profileService.getProfile(player.getUniqueId()) : null;
        Party party = profile != null ? profile.getParty() : null;
        if (party == null) {
            player.closeInventory();
            player.sendMessage(KaosPractice.getInstance().getService(LocaleService.class).getString(GlobalMessagesLocaleImpl.ERROR_YOU_NOT_IN_PARTY));
            return;
        }

        if (this.arena == null || !this.arena.isEnabled()) {
            player.closeInventory();
            player.sendMessage(CC.translate("&cEssa arena está indisponível agora."));
            return;
        }

        PartyService partyService = KaosPractice.getInstance().getService(PartyService.class);
        partyService.startMatch(this.kit, this.arena, party);
    }
}
