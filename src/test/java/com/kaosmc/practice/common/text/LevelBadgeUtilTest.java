package com.kaosmc.practice.common.text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelBadgeUtilTest {

    @Test
    void shouldMapInitialRangeToTenTier() {
        assertTrue(LevelBadgeUtil.getBadge(0).contains("[10"));
        assertTrue(LevelBadgeUtil.getBadge(10).contains("[10"));
    }

    @Test
    void shouldMapSecondRangeToTwentyTier() {
        assertTrue(LevelBadgeUtil.getBadge(11).contains("[20"));
        assertTrue(LevelBadgeUtil.getBadge(20).contains("[20"));
    }

    @Test
    void shouldRoundAboveOneThousandByHundreds() {
        assertTrue(LevelBadgeUtil.getBadge(1001).contains("[1100"));
        assertTrue(LevelBadgeUtil.getBadge(1099).contains("[1100"));
    }
}
