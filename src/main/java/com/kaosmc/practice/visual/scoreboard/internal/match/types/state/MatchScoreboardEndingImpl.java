package com.kaosmc.practice.visual.scoreboard.internal.match.types.state;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.PlayerDisplayUtil;
import com.kaosmc.practice.core.config.ConfigService;
import com.kaosmc.practice.feature.match.Match;
import com.kaosmc.practice.feature.match.model.internal.MatchGamePlayer;
import com.kaosmc.practice.feature.match.model.GameParticipant;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.visual.scoreboard.internal.match.MatchScoreboard;
import com.kaosmc.practice.common.text.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Kaos
 * @since 30/04/2025
 */
public class MatchScoreboardEndingImpl implements MatchScoreboard {
    @Override
    public List<String> getLines(Profile profile, Player player, GameParticipant<MatchGamePlayer> you, GameParticipant<MatchGamePlayer> opponent) {
        if (profile == null || profile.getProfileData() == null) {
            return Collections.emptyList();
        }
        if (player == null || !player.isOnline()) {
            return Collections.emptyList();
        }

        ConfigService configService = KaosPractice.getInstance().getService(ConfigService.class);
        if (configService == null || configService.getScoreboardConfig() == null || opponent == null || opponent.getLeader() == null || you == null || you.getLeader() == null) {
            return Collections.emptyList();
        }

        List<String> scoreboardLines = new ArrayList<>();
        Match match = profile.getMatch();
        if (match == null) return scoreboardLines;

        String opponentName = PlayerDisplayUtil.resolveCurrentNick(
                opponent.getLeader().getTeamPlayer(),
                opponent.getLeader().getUsername()
        );
        String winnerName = opponent.getLeader().isDead()
                ? PlayerDisplayUtil.resolveCurrentNick(you.getLeader().getTeamPlayer(), you.getLeader().getUsername())
                : opponentName;

        for (String line : configService.getScoreboardConfig().getStringList("scoreboard.lines.ending")) {
            scoreboardLines.add(CC.translate(line)
                    .replace("{opponent}", opponentName)
                    .replace("{duration}", match.getDuration())
                    .replace("{winner}", winnerName)
                    .replace("{end-result}", opponent.getLeader().isDead() ? "&a&lVICTORY!" : "&c&lDEFEAT!"));
        }

        return scoreboardLines;
    }
}
