package us.alleypvp.practice.feature.tournament.execution;

import us.alleypvp.practice.bootstrap.lifecycle.Service;
import us.alleypvp.practice.feature.tournament.engine.TournamentEvent;
import us.alleypvp.practice.feature.tournament.model.Tournament;

/**
 * @author Remi
 * @project alley-practice
 * @date 8/08/2025
 */
public interface TournamentExecutionStrategy extends Service {
    /**
     * Handles an external event (join, leave, match end, timers) during
     * tournament execution.
     *
     * @param tournament The tournament receiving the event.
     * @param event      The event to process.
     * @return The result of event processing.
     */
    ExecutionResult handleEvent(Tournament tournament, TournamentEvent event);
}