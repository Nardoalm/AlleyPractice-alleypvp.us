package us.alleypvp.practice.feature.match;

import us.alleypvp.practice.bootstrap.lifecycle.Service;
import us.alleypvp.practice.feature.arena.Arena;
import us.alleypvp.practice.feature.kit.Kit;
import us.alleypvp.practice.feature.match.model.GameParticipant;
import us.alleypvp.practice.feature.match.model.internal.MatchGamePlayer;
import us.alleypvp.practice.feature.tournament.model.Tournament;

import java.util.List;

public interface MatchService extends Service {
    List<Match> getMatches();

    void addMatch(Match match);

    void removeMatch(Match match);

    void createTournamentMatch(Tournament tournament, Kit kit, Arena arena, GameParticipant<MatchGamePlayer> pA, GameParticipant<MatchGamePlayer> pB);

    void createAndStartMatch(Kit kit, Arena arena, GameParticipant<MatchGamePlayer> participantA, GameParticipant<MatchGamePlayer> participantB, boolean teamMatch, boolean affectStatistics, boolean isRanked);
}