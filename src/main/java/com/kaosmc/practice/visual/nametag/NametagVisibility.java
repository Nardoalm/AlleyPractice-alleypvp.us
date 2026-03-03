package com.kaosmc.practice.visual.nametag;

import lombok.Getter;

/**
 * @author Remi
 * @project kaos-practice
 * @date 22/07/2025
 */
@Getter
public enum NametagVisibility {
    ALWAYS("always"),
    NEVER("never"),
    HIDE_FOR_OTHER_TEAMS("hideForOtherTeams"),
    HIDE_FOR_OWN_TEAM("hideForOwnTeam");

    ;

    private final String value;

    NametagVisibility(String value) {
        this.value = value;
    }

}
