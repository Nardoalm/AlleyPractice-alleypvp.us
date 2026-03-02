package com.kaosmc.practice.core.profile.menu.statistic.button;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.adapter.core.CoreAdapter;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.feature.level.LevelService;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.data.types.ProfileFFAData;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Kaos
 * @date 5/26/2024
 */
@RequiredArgsConstructor
public class GlobalStatButton extends Button {
    private final OfflinePlayer target;

    /**
     * Gets the item to display in the menu.
     *
     * @param player the player viewing the menu
     * @return the item to display
     */
    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = KaosPractice.getInstance().getService(ProfileService.class).getProfile(target.getUniqueId());

        CoreAdapter coreAdapter = KaosPractice.getInstance().getService(CoreAdapter.class);
        LevelService levelService = KaosPractice.getInstance().getService(LevelService.class);

        int ffaKills = profile.getProfileData().getFfaData().values().stream()
                .mapToInt(ProfileFFAData::getKills)
                .sum();
        int ffaDeaths = profile.getProfileData().getFfaData().values().stream()
                .mapToInt(ProfileFFAData::getDeaths)
                .sum();

        return new ItemBuilder(Material.SKULL_ITEM)
                .setSkull(target.getName())
                .name("&6&l" + target.getName() + " &r&7┃ &fStats")
                .lore(
                        CC.MENU_BAR,
                        "&7Showing global data.",
                        "",
                        "&6&lUnranked",
                        "&6│ &fWins: &6" + profile.getProfileData().getUnrankedWins(),
                        "&6│ &fLosses: &6" + profile.getProfileData().getUnrankedLosses(),
                        "",
                        "&6&lRanked",
                        "&6│ &fWins: &6" + profile.getProfileData().getRankedWins(),
                        "&6│ &fLosses: &6" + profile.getProfileData().getRankedLosses(),
                        "&6│ &fElo: &6" + profile.getProfileData().getElo(),
                        "",
                        "&6&lFFA",
                        "&6│ &fKills: &6" + ffaKills,
                        "&6│ &fDeaths: &6" + ffaDeaths,
                        "",
                        "&6&lAccount",
                        "&6│ &fRank: &6" + coreAdapter.getCore().getRankColor(target.getPlayer()) + coreAdapter.getCore().getRankName(target.getPlayer()),
                        "&6│ &fCoins: &6$" + profile.getProfileData().getCoins(),
                        "&6│ &fLevel: &6" + levelService.getLevel(profile.getProfileData().getGlobalLevel()).getDisplayName(),
                        CC.MENU_BAR

                )
                .build();
    }
}
