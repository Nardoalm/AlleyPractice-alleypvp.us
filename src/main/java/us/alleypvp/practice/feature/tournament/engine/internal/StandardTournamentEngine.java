package us.alleypvp.practice.feature.tournament.engine.internal;

import us.alleypvp.practice.bootstrap.annotation.Service;
import us.alleypvp.practice.feature.tournament.engine.TournamentConfiguration;
import us.alleypvp.practice.feature.tournament.engine.TournamentEngine;
import us.alleypvp.practice.feature.tournament.engine.TournamentEvent;
import us.alleypvp.practice.feature.tournament.execution.TournamentExecutionStrategy;
import us.alleypvp.practice.feature.tournament.model.Tournament;

/**
 * @author Remi
 * @project alley-practice
 * @date 8/08/2025
 */
@Service(provides = TournamentEngine.class, priority = 1000)
public class StandardTournamentEngine implements TournamentEngine {
    private final TournamentExecutionStrategy strategy;

    public StandardTournamentEngine(TournamentExecutionStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public Tournament initializeTournament(TournamentConfiguration configuration) {
        return new Tournament(
                configuration.getNumericId(),
                configuration.getHost().getName(),
                configuration.getKit(),
                configuration.getDisplayName(),
                configuration.getTeamSize(),
                configuration.getMaxTeams(),
                configuration.getMinTeams());
    }

    @Override
    public void processEvent(Tournament tournament, TournamentEvent event) {
        strategy.handleEvent(tournament, event);
    }
}