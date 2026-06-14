package us.alleypvp.practice.feature.tournament.formation;

import us.alleypvp.practice.bootstrap.lifecycle.Service;
import us.alleypvp.practice.feature.tournament.model.Tournament;
import us.alleypvp.practice.feature.tournament.model.TournamentParticipant;

import java.util.List;

public interface TournamentTeamFormationService extends Service {
    /**
     * Forms balanced teams for a tournament.
     *
     * @param tournament The tournament to form teams for
     * @return List of formed teams
     */
    List<TournamentParticipant> formTeamsForTournament(Tournament tournament);
}