package com.kaosmc.practice.feature.leaderboard;

import lombok.Getter;

/**
 * @author Emmy
 * @project Kaos
 * @date 17/11/2024 - 14:13
 */
@Getter
public enum LeaderboardType {
    UNRANKED("Unranked (Geral)"),
    UNRANKED_MONTHLY("Unranked (Mensal)"),
    WIN_STREAK("Win Streak"),
    FFA("FFA"),
    RANKED("Ranked"),
    TOURNAMENT("Torneio");

    private final String name;

    /**
     * Constructor for the EnumLeaderboardType.
     *
     * @param name The name of the leaderboard type.
     */
    LeaderboardType(String name) {
        this.name = name;
    }
}
