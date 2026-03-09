package com.kaosmc.practice.core.locale.internal.impl.message;

import com.kaosmc.practice.core.locale.LocaleEntry;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author Emmy
 * @project kaos-practice
 * @since 12/09/2025
 */
@Getter
public enum GameMessagesLocaleImpl implements LocaleEntry {
    DUEL_REQUEST_ACCEPTED("messages/game-messages.yml", "duel-requests.accepted", "&aVocê aceitou o pedido de duelo de &6{name-color}{player}&a!"),
    DUEL_REQUEST_SENT_SOLO("messages/game-messages.yml", "duel.request.sent.solo.format", Arrays.asList(
            "",
            "&6&lPedido de Duelo",
            " &6&l│ &fAlvo: &6{name-color}{target}",
            " &6&l│ &fKit: &6{kit}",
            " &6&l│ &fArena: &6{arena}",
            ""
    )),
    DUEL_REQUEST_SENT_PARTY("messages/game-messages.yml", "duel.request.sent.party.format", Arrays.asList(
            "",
            "&6&lPedido de Duelo de Party",
            " &6&l│ &fAlvo: &6Party de {name-color}{target} &6(&a{party-size}&6)",
            " &6&l│ &fKit: &6{kit}",
            " &6&l│ &fArena: &6{arena}",
            ""
    )),

    DUEL_REQUEST_RECEIVED_SOLO_CLICKABLE_FORMAT("messages/game-messages.yml", "duel.request.received.solo.clickable.format", " &a(Clique para aceitar)"),
    DUEL_REQUEST_RECEIVED_SOLO_CLICKABLE_COMMAND("messages/game-messages.yml", "duel.request.received.solo.clickable.command", "/accept {sender}"),
    DUEL_REQUEST_RECEIVED_SOLO_CLICKABLE_HOVER("messages/game-messages.yml", "duel.request.received.solo.clickable.hover", "&aClique para aceitar o pedido de duelo de &6{sender}&a."),
    DUEL_REQUEST_RECEIVED_SOLO("messages/game-messages.yml", "duel.request.received.solo.format", Arrays.asList(
            "",
            "&6&lPedido de Duelo",
            " &6&l│ &fDe: &6{name-color}{sender}",
            " &6&l│ &fArena: &6{arena}",
            " &6&l│ &fKit: &6{kit}",
            "{clickable}",
            ""
    )),

    DUEL_REQUEST_RECEIVED_PARTY_CLICKABLE_FORMAT("messages/game-messages.yml", "duel.request.received.party.clickable.format", " &a(Clique para aceitar)"),
    DUEL_REQUEST_RECEIVED_PARTY_CLICKABLE_COMMAND("messages/game-messages.yml", "duel.request.received.party.clickable.command", "/accept {sender}"),
    DUEL_REQUEST_RECEIVED_PARTY_CLICKABLE_HOVER("messages/game-messages.yml", "duel.request.received.party.clickable.hover", "&aClique para aceitar o pedido de duelo da party de &6{sender}&a."),
    DUEL_REQUEST_RECEIVED_PARTY("messages/game-messages.yml", "duel.request.received.party.format", Arrays.asList(
            "",
            "&6&lPedido de Duelo de Party",
            " &6&l│ &fDe: &6Party de {name-color}{sender} &6(&a{party-size}&6)",
            " &6&l│ &fArena: &6{arena}",
            " &6&l│ &fKit: &6{kit}",
            "{clickable}",
            ""
    )),

    DUEL_REQUEST_EXPIRED_ENABLED_BOOLEAN("messages/game-messages.yml", "duel.request.expired.sender.enabled", true),
    ERROR_DUEL_REQUESTS_EXPIRED("messages/game-messages.yml", "duel.request.expired.sender.format", Arrays.asList(
            "",
            "&c&lPedido de Duelo Expirado",
            " &c&l│ &fSeu pedido de duelo para &6{target} &fexpirou.",
            ""
    )),


    DUEL_REQUEST_EXPIRED_TARGET_ENABLED_BOOLEAN("messages/game-messages.yml", "duel.request.expired.target.enabled", true),
    DUEL_REQUEST_EXPIRED_TARGET("messages/game-messages.yml", "duel.request.expired.target.format", Arrays.asList(
            "",
            "&c&lPedido de Duelo Expirado",
            " &c&l│ &fO pedido de duelo de &6{sender} &fexpirou.",
            ""
    )),

    FFA_KILLSTREAK_ALERT_ENABLED_BOOLEAN("messages/game-messages.yml", "ffa.killstreak-alert.enabled", true),
    FFA_KILLSTREAK_ALERT_INTERVAL("messages/game-messages.yml", "ffa.killstreak-alert.interval", 5),
    FFA_KILLSTREAK_ALERT_MESSAGE("messages/game-messages.yml", "ffa.killstreak-alert.message", Arrays.asList(
            "",
            "&6&lKillstreak",
            " &6&l│ &f{name-color}{player} &festá em uma killstreak de &6{killstreak}&f!",
            ""
    )),

    FFA_COMBAT_LOG_DEATH_MESSAGE("messages/game-messages.yml", "ffa.combat-log.death-message", "&7(Combat Log) &c{name-color}{player} &ffoi morto por &c{killer-name-color}{killer}&f."),

    FFA_TELEPORTED_TO_SAFE_ZONE("messages/game-messages.yml", "ffa.teleported-to-safe-zone", "&aVocê foi teleportado para a safezone."),

    FFA_PLAYER_JOIN_MESSAGE_ENABLED_BOOLEAN("messages/game-messages.yml", "ffa.player.join-message.enabled", false),
    FFA_PLAYER_JOIN_MESSAGE_FORMAT("messages/game-messages.yml", "ffa.player.join-message.format", Collections.singletonList("&a{name-color}{player} &aentrou na partida de FFA.")),

    FFA_PLAYER_LEFT_MESSAGE_ENABLED_BOOLEAN("messages/game-messages.yml", "ffa.player.left-message.enabled", false),
    FFA_PLAYER_LEFT_MESSAGE_FORMAT("messages/game-messages.yml", "ffa.player.left-message.format", Collections.singletonList("&c{name-color}{player} &csaiu da partida de FFA.")),

    FFA_PLAYER_DIED_MESSAGE_ENABLED_BOOLEAN("messages/game-messages.yml", "ffa.player.death.no-killer-message.enabled", true),
    FFA_PLAYER_DIED_MESSAGE_FORMAT("messages/game-messages.yml", "ffa.player.death.no-killer-message.format", Collections.singletonList("&c{name-color}{player} &fmorreu!")),

    FFA_PLAYER_KILLED_PLAYER_MESSAGE_ENABLED_BOOLEAN("messages/game-messages.yml", "ffa.player.death.killed-message.enabled", true),
    FFA_PLAYER_KILLED_PLAYER_MESSAGE_FORMAT("messages/game-messages.yml", "ffa.player.death.killed-message.format", Collections.singletonList("&c{name-color}{player} &ffoi morto por &c{killer-name-color}{killer}&f!")),

    GAME_CANNOT_DROP_SWORD("messages/game-messages.yml", "game.cannot-drop-sword", "&cVocê não pode dropar sua espada!"),

    MATCH_DEATH_MESSAGE_GENERIC("messages/game-messages.yml", "match.death-message.generic", "&c&lMORTE! &f{name-color}{player} &fmorreu."),
    MATCH_DEATH_MESSAGE_GENERIC_KILLER("messages/game-messages.yml", "match.death-message.generic-killer", "&c&lMORTE! &f{name-color}{victim} &ffoi morto por &c{killer-name-color}{killer}&f."),
    MATCH_DEATH_MESSAGE_CUSTOM("messages/game-messages.yml", "match.death-message.custom", "&c&lMORTE! &f{message}"),

    MATCH_SEEKER_RESPAWNED("messages/game-messages.yml", "match.seeker-respawned", "&c&lMORTE! &fSeeker &c{name-color}{player} &frenasceu."),

    MATCH_STARTED_DISCLAIMER_ENABLED_BOOLEAN("messages/game-messages.yml", "match.started.kit-disclaimer.enabled", true),
    MATCH_STARTED_DISCLAIMER_FORMAT("messages/game-messages.yml", "match.started.kit-disclaimer.format", Arrays.asList(
            "",
            "&6&l{kit-name}",
            " &6&l│ &f{kit-disclaimer}",
            ""
    )),

    MATCH_ELEVATOR_NO_SAFE_SPOT_FOUND("messages/game-messages.yml", "match.elevator.no-safe-spot-found", "&cNenhum local seguro encontrado para teleportar!"),
    MATCH_CANNOT_INTERACT_DURING_MATCH("messages/game-messages.yml", "match.cannot-interact-during-match", "&cVocê não pode interagir com isso durante a partida!"),
    MATCH_CANNOT_INTERACT_AS_RAIDER("messages/game-messages.yml", "match.cannot-interact-as-raider", "&cVocê não pode interagir com isso como raider!"),
    MATCH_CANNOT_PLACE_BLOCKS_DURING_HIDING_PHASE("messages/game-messages.yml", "match.cannot-place-blocks-during-hiding-phase", "&cVocê não pode colocar blocos durante a fase de esconder!"),
    MATCH_CANNOT_PLACE_BLOCKS_ABOVE_HEIGHT_LIMIT("messages/game-messages.yml", "match.cannot-place-blocks-above-height-limit", "&cVocê não pode colocar blocos acima do limite de altura!"),
    MATCH_CANNOT_BUILD_NEAR_PORTAL("messages/game-messages.yml", "match.cannot-build-near-portal", "&cVocê não pode construir perto de um portal!"),
    MATCH_CANNOT_BREAK_BLOCKS_DURING_HIDING_PHASE("messages/game-messages.yml", "match.cannot-break-blocks-during-hiding-phase", "&cVocê não pode quebrar blocos durante a fase de esconder!"),
    MATCH_CANNOT_BREAK_OWN_BED("messages/game-messages.yml", "match.cannot-break-own-bed", "&cVocê não pode quebrar sua própria cama!"),

    MATCH_STARTED_MESSAGE_ENABLED_BOOLEAN("messages/game-messages.yml", "match.started.message.regular.enabled", true),
    MATCH_STARTED_MESSAGE_FORMAT("messages/game-messages.yml", "match.started.message.regular.format", Collections.singletonList("&aA partida começou!")),

    MATCH_ROUND_STARTED_MESSAGE_ENABLED_BOOLEAN("messages/game-messages.yml", "match.started.message.round.enabled", true),
    MATCH_ROUND_STARTED_MESSAGE_FORMAT("messages/game-messages.yml", "match.started.message.round.format", Collections.singletonList("&aRound &6{current-round} &acomeçou.")),

    MATCH_STARTING_MESSAGE_ENABLED_BOOLEAN("messages/game-messages.yml", "match.starting.message.enabled", true),
    MATCH_STARTING_MESSAGE_FORMAT("messages/game-messages.yml", "match.starting.message.format", Collections.singletonList("&a{stage}...")),

    MATCH_ROUND_STARTING_MESSAGE_ENABLED_BOOLEAN("messages/game-messages.yml", "match.round-starting.message.enabled", true),
    MATCH_ROUND_STARTING_MESSAGE_FORMAT("messages/game-messages.yml", "match.round-starting.message.format", Collections.singletonList("&a{stage}...")),

    MATCH_RESPAWNING_MESSAGE_ENABLED_BOOLEAN("messages/game-messages.yml", "match.respawning.message.enabled", true),
    MATCH_RESPAWNING_MESSAGE_FORMAT("messages/game-messages.yml", "match.respawning.message.format", Collections.singletonList("&a{seconds}...")),

    MATCH_TIME_LIMIT_EXCEEDED_FORMAT("messages/game-messages.yml", "match.ending.time-limit-exceeded.format", Collections.singletonList("&cA partida terminou por atingir o limite de &6{time-limit} minutos&c.")),

    MATCH_SCORED_MESSAGE_ENABLED_BOOLEAN("messages/game-messages.yml", "match.scored.enabled", true),
    MATCH_SCORED_MESSAGE_SOLO_FORMAT("messages/game-messages.yml", "match.scored.format.solo", Arrays.asList(
            ""
            , "{winner-color}&l{scorer} &6&lmarcou!"
            , "{winner-color}&l{winner-goals} &7- {loser-color}&l{loser-goals}"
            , ""
    )),
    MATCH_SCORED_MESSAGE_TEAM_FORMAT("messages/game-messages.yml", "match.scored.format.team", Arrays.asList(
            ""
            , "{winner-color}&lTime de {winner} &6&lmarcou!"
            , "{winner-color}&l{winner-goals} &7- {loser-color}&l{loser-goals}"
            , ""
    )),

    MATCH_BED_DESTRUCTION_MESSAGE_ENABLED_BOOLEAN("messages/game-messages.yml", "match.bed-destruction.enabled", true),
    MATCH_BED_DESTRUCTION_MESSAGE_FORMAT("messages/game-messages.yml", "match.bed-destruction.format", Arrays.asList(
            "",
            "&6&lCAMA DESTRUÍDA! &6{bed-color}{bed} &ffoi destruída por &6{breaker-color}{breaker}&f!",
            ""
    )),

    MATCH_ENDED_SPECTATORS_LIST("messages/game-messages.yml", "match.ended.spectators-list", Collections.singletonList("&6&lEspectadores: &f{spectators} &7(e mais &6{more_count} &7...)")),
    MATCH_ENDED_MATCH_RESULT_REGULAR_FORMAT("messages/game-messages.yml", "match.ended.match-result.regular.format", Arrays.asList(
            "",
            "&6&lResultado da Partida: &7(clique para ver)",
            "&aVencedor: &f{winner} &7| &cPerdedor: &f{loser}",
            ""
    )),
    MATCH_ENDED_MATCH_RESULT_REGULAR_WINNER_COMMAND("messages/game-messages.yml", "match.ended.match-result.regular.winner.command", "/inventory {winner}"),
    MATCH_ENDED_MATCH_RESULT_REGULAR_WINNER_HOVER("messages/game-messages.yml", "match.ended.match-result.regular.winner.hover", "&aClique para ver o inventário de {winner}"),
    MATCH_ENDED_MATCH_RESULT_REGULAR_LOSER_COMMAND("messages/game-messages.yml", "match.ended.match-result.regular.loser.command", "/inventory {loser}"),
    MATCH_ENDED_MATCH_RESULT_REGULAR_LOSER_HOVER("messages/game-messages.yml", "match.ended.match-result.regular.loser.hover", "&cClique para ver o inventário de {loser}"),

    MATCH_ENDED_MATCH_RESULT_ELO_CHANGES_ENABLED_BOOLEAN("messages/game-messages.yml", "match.ended.elo-changes.enabled", true),
    MATCH_ENDED_MATCH_RESULT_ELO_CHANGES_FORMAT("messages/game-messages.yml", "match.ended.elo-changes.format", Arrays.asList(
            "&6&lMudanças de Elo",
            " &6&l│ &f{winner} &a+{math-winner-elo} &7(&f{old-winner-elo} &7-> &f{new-winner-elo})",
            " &6&l│ &f{loser} &c-{math-loser-elo} &7(&f{old-loser-elo} &7-> &f{new-loser-elo})",
            ""
    )),

//    MATCH_DIVISION_PROGRESS_ENABLED_BOOLEAN("messages/game-messages.yml", "match.ended.division-progress.enabled", true),
//    MATCH_DIVISION_PROGRESS_ONGOING_FORMAT("messages/game-messages.yml", "match.ended.division-progress.format.ongoing", Arrays.asList(
//            "",
//            "&6&lProgress",
//            " &6&l│ &fUnlock &6{next-division} &fwith {wins-required} more {win-or-wins}!",
//            "  &7({progress-bar}&7) {progress-percentage}",
//            " &6&l│ &fDaily Streak: &6{daily-streak} &f(Best: {best-daily-streak})",
//            " &6&l│ &fWin Streak: &6{win-streak} &f(Best: {best-win-streak})",
//            ""
//    )),
//    MATCH_DIVISION_PROGRESS_REACHED_FORMAT("messages/game-messages.yml", "match.ended.division-progress.format.reached", Arrays.asList(
//            "",
//            "&a&lCONGRATULATIONS!",
//            " &6&l│ &fReached: &6{reached-new-division}",
//            " &6&l│ &fDaily Streak: &6{daily-streak} &f(Best: {best-daily-streak})",
//            " &6&l│ &fWin Streak: &6{win-streak} &f(Best: {best-win-streak})",
//            ""
//    )),

    MATCH_BLOCKS_RESET_MESSAGE_ENABLED_BOOLEAN("messages/game-messages.yml", "match.blocks-reset.enabled", true),
    MATCH_BLOCKS_RESET_MESSAGE_FORMAT("messages/game-messages.yml", "match.blocks-reset.format", Collections.singletonList("&4{name-color}{player} &fresetou os blocos!")),

    MATCH_PLATFORM_DECAY_WILL_NO_LONGER_DECAY("messages/game-messages.yml", "match.platform-decay.will-no-longer-decay", Collections.singletonList("&c&lA plataforma não irá mais decair!")),

    MATCH_PLATFORM_DECAY_NOTIFICATION_75_FORMAT("messages/game-messages.yml", "match.platform-decay.notifications.75.format", Collections.singletonList("&6A arena começou a desmoronar...")),
    MATCH_PLATFORM_DECAY_NOTIFICATION_50_FORMAT("messages/game-messages.yml", "match.platform-decay.notifications.50.format", Collections.singletonList("&e&lAVISO! &eA arena encolheu pela metade!")),
    MATCH_PLATFORM_DECAY_NOTIFICATION_25_FORMAT("messages/game-messages.yml", "match.platform-decay.notifications.25.format", Collections.singletonList("&c&lPERIGO! &cA plataforma está colapsando rápido!")),

    MATCH_PLAYER_VS_PLAYER_SOLO_ENABLED_BOOLEAN("messages/game-messages.yml", "match.versus-message.solo.enabled", true),
    MATCH_PLAYER_VS_PLAYER_SOLO_FORMAT("messages/game-messages.yml", "match.versus-message.solo.format", Collections.singletonList("&7[&6Match&7] &6{playerA} &avs &6{playerB}")),

    MATCH_PLAYER_VS_PLAYER_TEAM_ENABLED_BOOLEAN("messages/game-messages.yml", "match.versus-message.team.enabled", true),
    MATCH_PLAYER_VS_PLAYER_TEAM_FORMAT("messages/game-messages.yml", "match.versus-message.team.format", Collections.singletonList("&7[&6Match&7] &6Time de {teamA-leader} &7(&a{teamA-size}&7) &avs &6Time de {teamB-leader} &7(&a{teamB-size}&7)")),

    PARTY_INVITATION_RECEIVED_CLICKABLE_FORMAT("messages/game-messages.yml", "party.invitation.received.clickable.format", " &a(Clique para aceitar)"),
    PARTY_INVITATION_RECEIVED_CLICKABLE_COMMAND("messages/game-messages.yml", "party.invitation.received.clickable.command", "/party accept {sender}"),
    PARTY_INVITATION_RECEIVED_CLICKABLE_HOVER("messages/game-messages.yml", "party.invitation.received.clickable.hover", "&aClique para aceitar o convite de party de &6{sender}&a."),
    PARTY_INVITATION_RECEIVED("messages/game-messages.yml", "party.invitation.received.format", Arrays.asList(
            "",
            "&6&lConvite de Party",
            " &6&l│ &fDe: &6{name-color}{sender}",
            " &6&l│ &fJogadores: &6{party-size}&f/&6N/D",
            "{clickable}",
            ""
    )),

    PARTY_INVITATION_SENT("messages/game-messages.yml", "party.invitation.sent.format", Arrays.asList(
            "",
            "&6&lConvite de Party Enviado",
            " &6&l│ &fAlvo: &6{name-color}{target}",
            ""
    )),

    PARTY_ANNOUNCEMENT_CLICKABLE_FORMAT("messages/game-messages.yml", "party.announcement.clickable.format", " &a(Clique para entrar)"),
    PARTY_ANNOUNCEMENT_CLICKABLE_COMMAND("messages/game-messages.yml", "party.announcement.clickable.command", "/party join {leader}"),
    PARTY_ANNOUNCEMENT_CLICKABLE_HOVER("messages/game-messages.yml", "party.announcement.clickable.hover", "&aClique para entrar na party de &6{leader}&a."),
    PARTY_ANNOUNCEMENT("messages/game-messages.yml", "party.announcement.format", Arrays.asList(
            "",
            "&6&lConvite Público de Party",
            " &6&l│ &fLíder: &6{name-color}{leader}",
            " &6&l│ &fJogadores: &6{party-size}&f/&6N/D",
            "{clickable}",
            ""
    )),

    PARTY_REQUEST_EXPIRED_ENABLED_BOOLEAN("messages/game-messages.yml", "party.request.expired.sender.enabled", true),
    PARTY_REQUEST_EXPIRED("messages/game-messages.yml", "party.request.expired.sender.format", Arrays.asList(
            "",
            "&c&lPedido de Party Expirado",
            " &c&l│ &fSeu pedido de party para &6{target} &fexpirou.",
            ""
    )),
    PARTY_REQUEST_EXPIRED_TARGET_ENABLED_BOOLEAN("messages/game-messages.yml", "party.request.expired.target.enabled", true),
    PARTY_REQUEST_EXPIRED_TARGET("messages/game-messages.yml", "party.request.expired.target.format", Arrays.asList(
            "",
            "&c&lPedido de Party Expirado",
            " &c&l│ &fO pedido de party de &6{sender} &fexpirou.",
            ""
    )),

    ;

    private final String configName;
    private final String configPath;
    private final Object defaultValue;

    /**
     * Constructor for the GameMessagesLocaleImpl enum.
     *
     * @param configName   The name of the configuration file.
     * @param configPath   The path to the specific string within the configuration file.
     * @param defaultValue The default value for the locale entry.
     */
    GameMessagesLocaleImpl(String configName, String configPath, Object defaultValue) {
        this.configName = configName;
        this.configPath = configPath;
        this.defaultValue = defaultValue;
    }
}
