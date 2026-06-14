package us.alleypvp.practice.visual.scoreboard.internal.match.types.type;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.core.config.ConfigService;
import us.alleypvp.practice.feature.match.internal.types.BedMatch;
import us.alleypvp.practice.feature.match.internal.types.DefaultMatch;
import us.alleypvp.practice.feature.match.model.internal.MatchGamePlayer;
import us.alleypvp.practice.feature.match.model.GameParticipant;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.visual.scoreboard.internal.match.BaseMatchScoreboard;
import us.alleypvp.practice.visual.scoreboard.internal.match.annotation.ScoreboardData;
import us.alleypvp.practice.visual.scoreboard.utility.ScoreboardUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @since 26/06/2025
 */
@ScoreboardData(match = BedMatch.class)
public class MatchScoreboardBed extends BaseMatchScoreboard {
    @Override
    protected String getSoloConfigPath() {
        return "scoreboard.lines.playing.solo.bed-match";
    }

    @Override
    protected String getTeamConfigPath() {
        return "scoreboard.lines.playing.team.bed-match";
    }

    @Override
    protected String replacePlaceholders(String line, Profile profile, Player player, GameParticipant<MatchGamePlayer> you, GameParticipant<MatchGamePlayer> opponent) {
        String baseLine = super.replacePlaceholders(line, profile, player, you, opponent);
        DefaultMatch match = (DefaultMatch) profile.getMatch();

        String yourColorDisplay = getTeamDisplay(match.getTeamColor(you));
        String opponentColorDisplay = getTeamDisplay(match.getTeamColor(opponent));

        return baseLine
                .replace("{your-bed}", ScoreboardUtil.visualizeBed(you.isBedBroken()))
                .replace("{opponent-bed}", ScoreboardUtil.visualizeBed(opponent.isBedBroken()))
                .replace("{your-color-display}", yourColorDisplay)
                .replace("{opponent-color-display}", opponentColorDisplay);
    }

    private String getTeamDisplay(ChatColor teamColor) {
        if (teamColor == null) return "";
        String colorName = teamColor.name().toLowerCase();
        ConfigService configService = AlleyPractice.getInstance().getService(ConfigService.class);
        if (configService != null && configService.getScoreboardConfig() != null) {
            String configured = configService.getScoreboardConfig().getString("scoreboard.team-displays." + colorName, "");
            if (configured != null && !configured.trim().isEmpty()) {
                return configured;
            }
        }

        return teamColor + colorName.replace('_', ' ');
    }
}
