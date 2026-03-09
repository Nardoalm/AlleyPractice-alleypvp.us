package com.kaosmc.practice.common.text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelBadgeUtilTest {

    @Test
    void shouldMapInitialRangeToTenTier() {
        assertTrue(LevelBadgeUtil.getBadgeForLevel(0).contains("[10"));
        assertTrue(LevelBadgeUtil.getBadgeForLevel(10).contains("[10"));
    }

    @Test
    void shouldMapSecondRangeToTwentyTier() {
        assertTrue(LevelBadgeUtil.getBadgeForLevel(11).contains("[20"));
        assertTrue(LevelBadgeUtil.getBadgeForLevel(20).contains("[20"));
    }

    @Test
    void shouldChangeColorWhenBracketChanges() {
        assertTrue(LevelBadgeUtil.getBadgeForLevel(0).startsWith("\u00A77"));
        assertTrue(LevelBadgeUtil.getBadgeForLevel(11).startsWith("\u00A7a"));
        assertTrue(LevelBadgeUtil.getBadgeForLevel(21).startsWith("\u00A7b"));
    }

    @Test
    void shouldRoundAboveOneThousandByHundreds() {
        assertTrue(LevelBadgeUtil.getBadgeForLevel(1001).contains("[1100"));
        assertTrue(LevelBadgeUtil.getBadgeForLevel(1099).contains("[1100"));
    }
}
