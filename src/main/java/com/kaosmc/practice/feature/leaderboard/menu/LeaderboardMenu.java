package com.kaosmc.practice.feature.leaderboard.menu;

import com.google.common.collect.Maps;
import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.library.menu.Menu;
import com.kaosmc.practice.feature.kit.KitService;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.kit.setting.types.mode.KitSettingRanked;
import com.kaosmc.practice.feature.leaderboard.LeaderboardService;
import com.kaosmc.practice.feature.leaderboard.data.LeaderboardPlayerData;
import com.kaosmc.practice.feature.leaderboard.LeaderboardType;
import com.kaosmc.practice.feature.leaderboard.menu.button.DisplayTypeButton;
import com.kaosmc.practice.feature.leaderboard.menu.button.LeaderboardKitButton;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.menu.statistic.button.StatisticsButton;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project Kaos
 * @date 25/05/2024 - 14:51
 */
public class LeaderboardMenu extends Menu {
    protected final KaosPractice plugin = KaosPractice.getInstance();

    @Override
    public String getTitle(Player player) {
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        switch (profile.getLeaderboardType()) {
            case RANKED:
                return "&6&lRankings Ranked";
            case UNRANKED:
                return "&6&lRankings Unranked";
            case UNRANKED_MONTHLY:
                return "&6&lRankings Mensais";
            case FFA:
                return "&6&lRankings FFA";
            case TOURNAMENT:
                return "&6&lRankings de Torneio";
            case WIN_STREAK:
                return "&6&lRankings de Win Streak";
            default:
                return "&6&lRankings";
        }
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = Maps.newHashMap();
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        LeaderboardType currentType = profile.getLeaderboardType();
        LeaderboardService leaderboardService = KaosPractice.getInstance().getService(LeaderboardService.class);

        buttons.put(2, new StatisticsButton());
        buttons.put(6, new DisplayTypeButton());

        int slot = 10;  // declare slot here once

        for (Kit kit : KaosPractice.getInstance().getService(KitService.class).getKits()) {
            if (!kit.isEnabled() || kit.getIcon() == null) continue;

            List<LeaderboardPlayerData> leaderboard = leaderboardService.getLeaderboardEntries(kit, currentType);

            switch (currentType) {
                case RANKED:
                    if (!kit.isSettingEnabled(KitSettingRanked.class)) {
                        break;
                    }
                case UNRANKED:
                case UNRANKED_MONTHLY:
                case TOURNAMENT:
                case WIN_STREAK:
                case FFA:
                    slot = this.skipIfSlotCrossingBorder(slot);
                    buttons.put(slot++, new LeaderboardKitButton(kit, leaderboard, currentType));
                    break;
            }
        }

        this.addBorder(buttons, 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }
}
