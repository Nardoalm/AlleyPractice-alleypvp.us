package us.alleypvp.practice.feature.tournament.formation.assembly;

import us.alleypvp.practice.feature.tournament.formation.model.TeamDistribution;
import us.alleypvp.practice.feature.tournament.model.TournamentParticipant;

import java.util.List;

public interface TeamAssembler {
    List<TournamentParticipant> assembleTeams(List<TournamentParticipant> participantPool,
                                              TeamDistribution distribution,
                                              int maxTeamSize);
}