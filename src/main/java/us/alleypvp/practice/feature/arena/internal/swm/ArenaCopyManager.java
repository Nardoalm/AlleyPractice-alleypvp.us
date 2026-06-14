package us.alleypvp.practice.feature.arena.internal.swm;

import us.alleypvp.practice.feature.arena.internal.types.StandAloneArena;

public interface ArenaCopyManager {
    void initialize();

    boolean isEnabled();

    StandAloneArena createTemporaryArenaCopy(StandAloneArena originalArena, int copyId);

    boolean deleteTemporaryArena(StandAloneArena arena);

    void shutdown();
}
