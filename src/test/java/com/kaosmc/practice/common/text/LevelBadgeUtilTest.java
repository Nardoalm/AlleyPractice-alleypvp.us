package com.kaosmc.practice.common.text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelBadgeUtilTest {

    @Test
    void shouldKeepExactLevelNumberInBadge() {
        assertTrue(LevelBadgeUtil.getBadgeForLevel(0).contains("[0"));
        assertTrue(LevelBadgeUtil.getBadgeForLevel(7).contains("[7"));
        assertTrue(LevelBadgeUtil.getBadgeForLevel(13).contains("[13"));
    }

    @Test
    void shouldStillChangeStyleByBracket() {
        assertTrue(LevelBadgeUtil.getBadgeForLevel(10).startsWith("\u00A77"));
        assertTrue(LevelBadgeUtil.getBadgeForLevel(11).startsWith("\u00A7a"));
        assertTrue(LevelBadgeUtil.getBadgeForLevel(21).startsWith("\u00A7b"));
    }

    @Test
    void shouldReturnExactNumericLevelFromExperience() {
        assertEquals(0, LevelBadgeUtil.getLevel(0));
        assertEquals(1, LevelBadgeUtil.getLevel(20));
        assertEquals(10, LevelBadgeUtil.getLevel(200));
        assertEquals(11, LevelBadgeUtil.getLevel(230));
    }

    @Test
    void shouldKeepExactNumberAboveOneThousand() {
        assertTrue(LevelBadgeUtil.getBadgeForLevel(1001).contains("[1001"));
        assertTrue(LevelBadgeUtil.getBadgeForLevel(1099).contains("[1099"));
    }
}
