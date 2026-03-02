package com.kaosmc.practice.visual.scoreboard.internal.match.types.type;

import com.kaosmc.practice.feature.kit.setting.types.mode.KitSettingStickFight;
import com.kaosmc.practice.feature.match.internal.types.RoundsMatch;
import com.kaosmc.practice.feature.match.model.internal.MatchGamePlayer;
import com.kaosmc.practice.feature.match.model.GameParticipant;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.visual.scoreboard.internal.match.BaseMatchScoreboard;
import com.kaosmc.practice.visual.scoreboard.internal.match.annotation.ScoreboardData;
import com.kaosmc.practice.visual.scoreboard.utility.ScoreboardUtil;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Kaos
 * @since 26/06/2025
 */
@ScoreboardData(kit = KitSettingStickFight.class)
public class MatchScoreboardStickFight extends BaseMatchScoreboard {
    @Override
    protected String getSoloConfigPath() {
        return "scoreboard.lines.playing.solo.stickfight-match";
    }

    @Override
    protected String getTeamConfigPath() {
        return "scoreboard.lines.playing.team.stickfight-match";
    }

    @Override
    protected String replacePlaceholders(String line, Profile profile, Player player, GameParticipant<MatchGamePlayer> you, GameParticipant<MatchGamePlayer> opponent) {
        String baseLine = super.replacePlaceholders(line, profile, player, you, opponent);
        RoundsMatch roundsMatch = (RoundsMatch) profile.getMatch();

        return baseLine
                .replace("{goals}", ScoreboardUtil.visualizeGoals(roundsMatch.getParticipantA().getLeader().getData().getScore(), 5))
                .replace("{opponent-goals}", ScoreboardUtil.visualizeGoals(roundsMatch.getParticipantB().getLeader().getData().getScore(), 5))
                .replace("{current-round}", String.valueOf(roundsMatch.getCurrentRound()))
                .replace("{color}", String.valueOf(roundsMatch.getTeamAColor()))
                .replace("{opponent-color}", String.valueOf(roundsMatch.getTeamBColor()));
    }
}