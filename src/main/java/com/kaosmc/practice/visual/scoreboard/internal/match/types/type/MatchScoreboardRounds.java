package com.kaosmc.practice.visual.scoreboard.internal.match.types.type;

import com.kaosmc.practice.feature.match.internal.types.RoundsMatch;
import com.kaosmc.practice.feature.match.model.internal.MatchGamePlayer;
import com.kaosmc.practice.feature.match.model.GameParticipant;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.visual.scoreboard.internal.match.BaseMatchScoreboard;
import com.kaosmc.practice.visual.scoreboard.internal.match.annotation.ScoreboardData;
import com.kaosmc.practice.common.time.TimeUtil;
import com.kaosmc.practice.visual.scoreboard.utility.ScoreboardUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Remi
 * @project Kaos
 * @since 26/06/2025
 */
@ScoreboardData(match = RoundsMatch.class)
public class MatchScoreboardRounds extends BaseMatchScoreboard {
    @Override
    protected String getSoloConfigPath() {
        return "scoreboard.lines.playing.solo.rounds-match";
    }

    @Override
    protected String getTeamConfigPath() {
        return "scoreboard.lines.playing.team.rounds-match";
    }

    @Override
    protected String replacePlaceholders(String line, Profile profile, Player player, GameParticipant<MatchGamePlayer> you, GameParticipant<MatchGamePlayer> opponent) {
        String baseLine = super.replacePlaceholders(line, profile, player, you, opponent);
        RoundsMatch roundsMatch = (RoundsMatch) profile.getMatch();

        return baseLine
                .replace("{time-left}", getFormattedTime(profile))
                .replace("{goals}", ScoreboardUtil.visualizeGoals(roundsMatch.getParticipantA().getLeader().getData().getScore(), 3))
                .replace("{opponent-goals}", ScoreboardUtil.visualizeGoals(roundsMatch.getParticipantB().getLeader().getData().getScore(), 3))
                .replace("{kills}", String.valueOf(profile.getMatch().getGamePlayer(player).getData().getKills()))
                .replace("{current-round}", String.valueOf(roundsMatch.getCurrentRound()))
                .replace("{color}", String.valueOf(roundsMatch.getTeamAColor()))
                .replace("{opponent-color}", String.valueOf(roundsMatch.getTeamBColor()));
    }

    private @NotNull String getFormattedTime(Profile profile) {
        long elapsedTime = System.currentTimeMillis() - profile.getMatch().getStartTime();
        long remainingTime = Math.max(900_000 - elapsedTime, 0);
        return TimeUtil.millisToFourDigitSecondsTimer(remainingTime);
    }
}