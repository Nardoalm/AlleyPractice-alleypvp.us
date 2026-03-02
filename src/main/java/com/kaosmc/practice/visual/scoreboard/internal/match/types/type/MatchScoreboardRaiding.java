package com.kaosmc.practice.visual.scoreboard.internal.match.types.type;

import com.kaosmc.practice.feature.kit.setting.types.mode.KitSettingRaiding;
import com.kaosmc.practice.feature.match.model.internal.MatchGamePlayer;
import com.kaosmc.practice.feature.match.model.GameParticipant;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.visual.scoreboard.internal.match.BaseMatchScoreboard;
import com.kaosmc.practice.visual.scoreboard.internal.match.annotation.ScoreboardData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Kaos
 * @since 26/06/2025
 */
@ScoreboardData(kit = KitSettingRaiding.class)
public class MatchScoreboardRaiding extends BaseMatchScoreboard {
    @Override
    protected String getSoloConfigPath() {
        return "scoreboard.lines.playing.solo.raiding-match";
    }

    @Override
    protected String getTeamConfigPath() {
        return "scoreboard.lines.playing.team.raiding-match";
    }

    @Override
    protected String replacePlaceholders(String line, Profile profile, Player player, GameParticipant<MatchGamePlayer> you, GameParticipant<MatchGamePlayer> opponent) {
        String baseLine = super.replacePlaceholders(line, profile, player, you, opponent);

        return baseLine
                .replace("{role}", you.getLeader().getData().getRole().getDisplayName());
    }
}
