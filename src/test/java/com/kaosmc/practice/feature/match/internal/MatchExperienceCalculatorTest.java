package com.kaosmc.practice.feature.match.internal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MatchExperienceCalculatorTest {

    @Test
    void shouldReturnBaseXpForUnrankedWithoutStreak() {
        assertEquals(20, MatchExperienceCalculator.calculateWinXp(false, 0));
    }

    @Test
    void shouldApplyRankedMultiplierWithoutStreak() {
        assertEquals(30, MatchExperienceCalculator.calculateWinXp(true, 0));
    }

    @Test
    void shouldCapStreakBonusAtHundredPercentInUnranked() {
        assertEquals(40, MatchExperienceCalculator.calculateWinXp(false, 50));
    }

    @Test
    void shouldScaleWithStreakInRanked() {
        assertEquals(53, MatchExperienceCalculator.calculateWinXp(true, 15));
    }

    @Test
    void shouldCapStreakBonusAtHundredPercentInRanked() {
        assertEquals(60, MatchExperienceCalculator.calculateWinXp(true, 999));
    }

    @Test
    void shouldTreatNegativeStreakAsZero() {
        assertEquals(20, MatchExperienceCalculator.calculateWinXp(false, -5));
    }
}
