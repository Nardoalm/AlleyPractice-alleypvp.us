package us.alleypvp.practice.feature.queue;

import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 13/03/2025
 */
@Getter
public enum QueueType {
    UNRANKED("Fila Solo Unranked"),
    DUOS("Fila Duo Unranked"),
    BOTS("Fila de Bots Unranked"),
    FFA("Fila de FFA Unranked"),

    ;

    private final String menuTitle;

    /**
     * Constructor for the EnumQueueType class.
     *
     * @param menuTitle The title of the menu.
     */
    QueueType(String menuTitle) {
        this.menuTitle = menuTitle;
    }
}
