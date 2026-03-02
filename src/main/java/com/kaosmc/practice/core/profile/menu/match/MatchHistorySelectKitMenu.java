package com.kaosmc.practice.core.profile.menu.match;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.library.menu.Menu;
import com.kaosmc.practice.feature.kit.KitService;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.match.data.MatchData;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.menu.match.button.MatchHistorySelectKitButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Kaos
 * @since 04/06/2025
 */
public class MatchHistorySelectKitMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&6&lMatch History";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Profile profile = KaosPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());

        List<MatchData> matchDataList = profile.getProfileData().getPreviousMatches();

        List<Kit> kits = this.plugin.getService(KitService.class).getKits();
        List<Kit> matchedKitsWithData = kits.stream()
                .filter(kit -> matchDataList.stream().anyMatch(matchData -> matchData.getKit().equals(kit.getName())))
                .collect(Collectors.toList());

        matchedKitsWithData.forEach(kit -> buttons.put(buttons.size(), new MatchHistorySelectKitButton(kit)));

        return buttons;
    }
}
