package com.kaosmc.practice.core.profile.menu.statistic;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.library.menu.Menu;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.data.types.ProfileFFAData;
import com.kaosmc.practice.core.profile.data.types.ProfileRankedKitData;
import com.kaosmc.practice.core.profile.data.types.ProfileUnrankedKitData;
import com.kaosmc.practice.core.profile.menu.statistic.button.GlobalStatButton;
import com.kaosmc.practice.core.profile.menu.statistic.button.LeaderboardButton;
import com.kaosmc.practice.core.profile.menu.statistic.button.StatisticsButton;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import lombok.AllArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author Emmy
 * @project Kaos
 * @date 17/11/2024 - 12:25
 */
@AllArgsConstructor
public class StatisticsMenu extends Menu {
    private OfflinePlayer target;

    @Override
    public String getTitle(Player player) {
        return this.target == player ? "&6&lYour Stats" : "&6&l" + this.target.getName() + "'s Stats";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(2, new StatisticsButton());
        buttons.put(4, new GlobalStatButton(target));
        buttons.put(6, new LeaderboardButton());
        //buttons.put(8, new DivisionViewButton(Kaos.getInstance().getService(IProfileService.class).getProfile(player.getUniqueId())));

        Profile profile = KaosPractice.getInstance().getService(ProfileService.class).getProfile(this.target == player ? player.getUniqueId() : this.target.getUniqueId());
        List<Kit> sortedKits = profile.getSortedKits();

        int slot = 10;
        for (Kit kit : sortedKits) {
            buttons.put(slot++, new KitStatButton(profile, kit));
            if (slot == 17 || slot == 26 || slot == 35 || slot == 44 || slot == 53) {
                slot += 2;
            }
        }

        this.addBorder(buttons, 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }

    @AllArgsConstructor
    private static class KitStatButton extends Button {
        private final Profile profile;
        private final Kit kit;

        @Override
        public ItemStack getButtonItem(Player player) {
            ProfileRankedKitData profileRankedKitData = this.profile.getProfileData().getRankedKitData().get(this.kit.getName());
            ProfileUnrankedKitData profileUnrankedKitData = this.profile.getProfileData().getUnrankedKitData().get(this.kit.getName());
            ProfileFFAData profileFFAData = this.profile.getProfileData().getFfaData().get(this.kit.getName());

            List<String> lore = new ArrayList<>(Arrays.asList(
                    CC.MENU_BAR,
                    "&6&lUnranked &6⭐" + profileUnrankedKitData.getDivision().getName() + " " + profileUnrankedKitData.getTier().getName(),
                    "&6│ &fWins: &6" + profileUnrankedKitData.getWins(),
                    //"&f● &6Losses: &f" + profileUnrankedKitData.getLosses(),
                    "",
                    "&6│ &fWin Streak: " + "&6N/A",
                    "    &fBest: " + "&6N/A" + " &7(N/A Daily)"
            ));

            if (this.profile.hasParticipatedInRanked()) {
                lore.addAll(Arrays.asList(
                        "",
                        "&6&lRanked",
                        "&6│ &fWins: &6" + profileRankedKitData.getWins(),
                        //"&f● &6Losses: &f" + profileRankedKitData.getLosses(),
                        "&6│ &fElo: &6" + profileRankedKitData.getElo()
                ));
            }

            if (this.profile.hasParticipatedInTournament()) {
                lore.addAll(Arrays.asList(
                        "",
                        "&6&lTournament",
                        "&f● &6Wins: &f" + "N/A",
                        "&f● &6Losses: &f" + "N/A"
                ));
            }

            if (this.kit.isFfaEnabled() && this.profile.hasParticipatedInFFA()) {
                lore.addAll(Arrays.asList(
                        "",
                        "&6&lFFA",
                        "&f● &6Kills: &f" + profileFFAData.getKills() + " &7(" + profileFFAData.getKillDeathRatio() + ")",
                        "&f● &6Deaths: &f" + profileFFAData.getDeaths()
                ));
            }

            lore.add(CC.MENU_BAR);

            return new ItemBuilder(this.kit.getIcon())
                    .name("&6&l" + this.kit.getDisplayName())
                    .durability(this.kit.getDurability())
                    .lore(lore)
                    .hideMeta()
                    .build();
        }
    }
}