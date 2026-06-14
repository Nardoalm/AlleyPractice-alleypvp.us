package us.alleypvp.practice.feature.tournament.match;

import us.alleypvp.practice.bootstrap.lifecycle.Service;
import us.alleypvp.practice.feature.match.model.GameParticipant;
import us.alleypvp.practice.feature.match.model.internal.MatchGamePlayer;
import us.alleypvp.practice.feature.tournament.model.TournamentParticipant;

/**
 * @author Remi
 * @project alley-practice
 * @date 8/08/2025
 */
public interface MatchParticipantFactory extends Service {
    /**
     * Creates a GameParticipant for a tournament team, producing a
     * TeamGameParticipant for teams (>1) or a solo participant for 1.
     *
     * @param participant The tournament team.
     * @return The constructed GameParticipant.
     */
    GameParticipant<MatchGamePlayer> buildParticipant(TournamentParticipant participant);
}