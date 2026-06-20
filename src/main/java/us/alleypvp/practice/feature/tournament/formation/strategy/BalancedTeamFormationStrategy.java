package us.alleypvp.practice.feature.tournament.formation.strategy;


import us.alleypvp.practice.feature.tournament.formation.TeamFormationStrategy;
import us.alleypvp.practice.feature.tournament.formation.assembly.TeamAssembler;
import us.alleypvp.practice.feature.tournament.formation.assembly.internal.PartyAwareTeamAssembler;
import us.alleypvp.practice.feature.tournament.formation.distribution.TeamDistributionCalculator;
import us.alleypvp.practice.feature.tournament.formation.distribution.internal.OptimalDistributionCalculator;
import us.alleypvp.practice.feature.tournament.formation.model.TeamDistribution;
import us.alleypvp.practice.feature.tournament.model.TournamentParticipant;

import java.util.List;

public class BalancedTeamFormationStrategy implements TeamFormationStrategy {
    private final TeamDistributionCalculator distributionCalculator;
    private final TeamAssembler teamAssembler;

    public BalancedTeamFormationStrategy() {
        this.distributionCalculator = new OptimalDistributionCalculator();
        this.teamAssembler = new PartyAwareTeamAssembler();
    }

    @Override
    public List<TournamentParticipant> formTeams(List<TournamentParticipant> participantPool, int maxTeamSize) {
        TeamDistribution distribution = distributionCalculator.calculateDistribution(participantPool, maxTeamSize);

        return teamAssembler.assembleTeams(participantPool, distribution, maxTeamSize);
    }
}