package com.kaosmc.practice.core.profile.menu.match.button;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.match.data.MatchData;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.menu.match.MatchHistoryViewMenu;
import com.kaosmc.practice.common.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Emmy
 * @project Kaos
 * @since 04/06/2025
 */
@AllArgsConstructor
public class MatchHistorySelectKitButton extends Button {
    protected final Kit kit;

    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = KaosPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());

        List<MatchData> matchDataList = profile.getProfileData().getPreviousMatches();

        int count = (int) matchDataList.stream()
                .filter(matchData -> matchData.getKit().equals(this.kit.getName()))
                .count();

        int unrankedCount = (int) matchDataList.stream()
                .filter(matchData -> matchData.getKit().equals(this.kit.getName()) && !matchData.isRanked())
                .count();

        int rankedCount = count - unrankedCount;

        return new ItemBuilder(this.kit.getIcon())
                .name("&6&l" + this.kit.getDisplayName())
                .lore(
                        " &f● Total: &6" + count,
                        " &f● Ranked: &6" + rankedCount,
                        " &f● Unranked: &6" + unrankedCount,
                        "",
                        "&aClique para visualizar!"
                )
                .durability(this.kit.getDurability())
                .hideMeta()
                .amount(count > 0 ? count : 1)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        new MatchHistoryViewMenu(this.kit).openMenu(player);
    }
}
