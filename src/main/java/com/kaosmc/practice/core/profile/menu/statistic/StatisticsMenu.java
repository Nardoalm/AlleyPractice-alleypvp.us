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
import com.kaosmc.practice.feature.division.model.DivisionTier;
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
        return this.target == player ? "&6&lSuas Estatísticas" : "&6&lEstatísticas de " + this.target.getName();
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
            buttons.put(slot++, new KitStatButton(this.target.getUniqueId(), kit));
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
        private static final int DEFAULT_ELO = 1000;

        private final UUID targetUuid;
        private final Kit kit;

        @Override
        public ItemStack getButtonItem(Player player) {
            ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
            Profile targetProfile = profileService != null ? profileService.getProfile(this.targetUuid) : null;

            ProfileRankedKitData profileRankedKitData = null;
            ProfileUnrankedKitData profileUnrankedKitData = null;
            ProfileFFAData profileFFAData = null;

            if (targetProfile != null && targetProfile.getProfileData() != null) {
                if (targetProfile.getProfileData().getRankedKitData() != null) {
                    profileRankedKitData = targetProfile.getProfileData().getRankedKitData().get(this.kit.getName());
                }
                if (targetProfile.getProfileData().getUnrankedKitData() != null) {
                    profileUnrankedKitData = targetProfile.getProfileData().getUnrankedKitData().get(this.kit.getName());
                }
                if (targetProfile.getProfileData().getFfaData() != null) {
                    profileFFAData = targetProfile.getProfileData().getFfaData().get(this.kit.getName());
                }
            }

            int unrankedWins = profileUnrankedKitData != null ? profileUnrankedKitData.getWins() : 0;
            int rankedWins = profileRankedKitData != null ? profileRankedKitData.getWins() : 0;
            int rankedElo = profileRankedKitData != null ? profileRankedKitData.getElo() : DEFAULT_ELO;

            String divisionName = "Unranked";
            String tierName = "I";
            if (profileUnrankedKitData != null) {
                if (profileUnrankedKitData.getDivision() != null) {
                    if (profileUnrankedKitData.getDivision().getDisplayName() != null && !profileUnrankedKitData.getDivision().getDisplayName().isEmpty()) {
                        divisionName = profileUnrankedKitData.getDivision().getDisplayName();
                    } else if (profileUnrankedKitData.getDivision().getName() != null) {
                        divisionName = profileUnrankedKitData.getDivision().getName();
                    }
                }
                DivisionTier tier = profileUnrankedKitData.getTier();
                if (tier != null && tier.getName() != null) {
                    tierName = tier.getName();
                }
            }

            List<String> lore = new ArrayList<>(Arrays.asList(
                    CC.MENU_BAR,
                    "&6&lUnranked &6⭐" + divisionName + " " + tierName,
                    "&6│ &fVitórias: &6" + unrankedWins,
                    //"&f● &6Losses: &f" + profileUnrankedKitData.getLosses(),
                    "",
                    "&6│ &fWin Streak: " + "&6N/D",
                    "    &fMelhor: " + "&6N/D" + " &7(N/D Diário)"
            ));

            if (targetProfile != null && targetProfile.hasParticipatedInRanked()) {
                lore.addAll(Arrays.asList(
                        "",
                        "&6&lRanked",
                        "&6│ &fVitórias: &6" + rankedWins,
                        //"&f● &6Losses: &f" + profileRankedKitData.getLosses(),
                        "&6│ &fElo: &6" + rankedElo
                ));
            }

            if (targetProfile != null && targetProfile.hasParticipatedInTournament()) {
                lore.addAll(Arrays.asList(
                        "",
                        "&6&lTorneio",
                        "&f● &6Vitórias: &f" + "N/D",
                        "&f● &6Derrotas: &f" + "N/D"
                ));
            }

            if (this.kit.isFfaEnabled() && targetProfile != null && targetProfile.hasParticipatedInFFA()) {
                int kills = profileFFAData != null ? profileFFAData.getKills() : 0;
                int deaths = profileFFAData != null ? profileFFAData.getDeaths() : 0;
                String kd = profileFFAData != null ? profileFFAData.getKillDeathRatio() : "0.0x";
                lore.addAll(Arrays.asList(
                        "",
                        "&6&lFFA",
                        "&f● &6Kills: &f" + kills + " &7(" + kd + ")",
                        "&f● &6Deaths: &f" + deaths
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
