package com.kaosmc.practice.core.profile.data.types;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProfileUnrankedKitDataTest {

    @Test
    void shouldTrackCurrentAndBestWinstreak() {
        ProfileUnrankedKitData data = new ProfileUnrankedKitData();

        data.incrementWins();
        data.incrementWins();
        data.incrementLosses();
        data.incrementWins();

        assertEquals(1, data.getWinstreak());
        assertEquals(2, data.getBestWinstreak());
        assertEquals(3, data.getWins());
        assertEquals(1, data.getLosses());
    }
}
