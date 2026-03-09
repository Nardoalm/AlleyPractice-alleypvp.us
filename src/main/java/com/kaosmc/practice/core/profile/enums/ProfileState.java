package com.kaosmc.practice.core.profile.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project Kaos
 * @date 5/21/2024
 */
@Getter
public enum ProfileState {
    LOBBY("Lobby", "O jogador está no lobby"),
    WAITING("Aguardando", "O jogador está aguardando na fila por um oponente"),
    PLAYING("Jogando", "O jogador está em uma partida"),
    FIGHTING_BOT("Lutando com Bot", "O jogador está lutando contra um bot"),
    SPECTATING("Espectando", "O jogador está assistindo a uma partida"),
    EDITING("Editando", "O jogador está editando um kit"),
    PLAYING_TOURNAMENT("Torneio", "O jogador está em um torneio"),
    PLAYING_EVENT("Evento", "O jogador está em um evento"),
    FFA("FFA", "O jogador está no lobby do FFA"),

    ;

    private final String name;
    private final String description;

    /**
     * Constructor for the EnumProfileState enum.
     *
     * @param name        The name of the profile state.
     * @param description The description of the profile state.
     */
    ProfileState(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
