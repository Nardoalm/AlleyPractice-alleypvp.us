package us.alleypvp.practice.feature.tournament.formation.distribution;

import us.alleypvp.practice.feature.tournament.formation.model.TeamDistribution;
import us.alleypvp.practice.feature.tournament.model.TournamentParticipant;

import java.util.List;

public interface TeamDistributionCalculator {
    TeamDistribution calculateDistribution(List<TournamentParticipant> participantPool, int maxTeamSize);
}

