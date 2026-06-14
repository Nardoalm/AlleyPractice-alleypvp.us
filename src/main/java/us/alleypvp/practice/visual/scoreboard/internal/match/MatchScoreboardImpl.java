package us.alleypvp.practice.visual.scoreboard.internal.match;

import us.alleypvp.practice.feature.match.Match;
import us.alleypvp.practice.feature.match.MatchState;
import us.alleypvp.practice.feature.match.model.internal.MatchGamePlayer;
import us.alleypvp.practice.feature.match.model.GameParticipant;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.visual.scoreboard.Scoreboard;
import us.alleypvp.practice.visual.scoreboard.internal.match.types.state.MatchScoreboardEndingImpl;
import us.alleypvp.practice.visual.scoreboard.internal.match.types.state.MatchScoreboardStartingImpl;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public class MatchScoreboardImpl implements Scoreboard {
    private final MatchScoreboardRegistry registry;

    private final MatchScoreboardStartingImpl matchScoreboardStarting;
    private final MatchScoreboardEndingImpl matchScoreboardEnding;

    /**
     * Constructor for the MatchScoreboard class.
     * It instantiates the registry, which automatically discovers all providers.
     */
    public MatchScoreboardImpl() {
        this.registry = new MatchScoreboardRegistry();
        this.registry.initialize();

        this.matchScoreboardStarting = new MatchScoreboardStartingImpl();
        this.matchScoreboardEnding = new MatchScoreboardEndingImpl();
    }

    @Override
    public List<String> getLines(Profile profile) {
        return Collections.emptyList();
    }

    @Override
    public List<String> getLines(Profile profile, Player player) {
        Match match = profile.getMatch();

        GameParticipant<MatchGamePlayer> you = match.getParticipant(player);
        GameParticipant<MatchGamePlayer> opponent = match.getOpponent(player);
        if (you == null || opponent == null) {
            return Collections.emptyList();
        }

        if (match.getState() == MatchState.STARTING) {
            return matchScoreboardStarting.getLines(profile, player, you, opponent);
        }
        if (match.getState() == MatchState.ENDING_MATCH) {
            return matchScoreboardEnding.getLines(profile, player, you, opponent);
        }

        MatchScoreboard scoreboardImpl = registry.getScoreboard(match);
        if (scoreboardImpl == null) {
            return Collections.emptyList();
        }

        return scoreboardImpl.getLines(profile, player, you, opponent);
    }
}