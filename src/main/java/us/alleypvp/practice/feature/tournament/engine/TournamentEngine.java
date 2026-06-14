package us.alleypvp.practice.feature.tournament.engine;

import us.alleypvp.practice.bootstrap.lifecycle.Service;
import us.alleypvp.practice.feature.tournament.model.Tournament;

/**
 * @author Remi
 * @project alley-practice
 * @date 8/08/2025
 */
public interface TournamentEngine extends Service {
    /**
     * Initializes and returns a new tournament from configuration.
     *
     * @param configuration The tournament setup configuration.
     * @return The initialized tournament instance.
     */
    Tournament initializeTournament(TournamentConfiguration configuration);

    /**
     * Processes an external event against a tournament, advancing or mutating
     * its lifecycle via the configured strategy.
     *
     * @param tournament The tournament to mutate.
     * @param event      The event that occurred.
     */
    void processEvent(Tournament tournament, TournamentEvent event);
}