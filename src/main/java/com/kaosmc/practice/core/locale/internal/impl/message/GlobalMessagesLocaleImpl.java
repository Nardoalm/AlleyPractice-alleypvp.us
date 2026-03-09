package com.kaosmc.practice.core.locale.internal.impl.message;

import com.kaosmc.practice.common.text.Symbol;
import com.kaosmc.practice.core.locale.LocaleEntry;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author ysubz
 * @project Kaos
 * @since 09/09/2025
 */
@Getter
public enum GlobalMessagesLocaleImpl implements LocaleEntry {
    ARENA_NOT_FOUND("messages/global-messages.yml", "arena.error.not-found", "&cNão existe uma arena chamada &6{arena-name}&c!"),
    ARENA_ALREADY_EXISTS("messages/global-messages.yml", "arena.error.already-exists", "&cJá existe uma arena com esse nome!"),
    ARENA_NO_SELECTION("messages/global-messages.yml", "arena.error.no-selection", "&cVocê não selecionou os limites da arena! Use &b/arena wand&c!"),
    ARENA_KIT_ALREADY_ADDED("messages/global-messages.yml", "arena.error.kit-already-added-to-arena", "&cO kit &6{kit-name} &cjá foi adicionado à arena &6{arena-name}&c!"),
    ARENA_ARENA_DOES_NOT_HAVE_KIT("messages/global-messages.yml", "arena.error.arena-does-not-have-kit", "&cA arena &6{arena-name} &cnão possui o kit &6{kit-name}&c!"),
    ARENA_CAN_NOT_SET_CUBOID_FFA("messages/global-messages.yml", "arena.error.cannot-set-cuboid-ffa", "&cVocê não pode definir cuboides para arenas de FFA! Use &4/arena setsafezone pos1/pos2&c."),
    ARENA_MUST_BE_STANDALONE("messages/global-messages.yml", "arena.error.must-be-standalone", "&cA arena &6{arena-name} &cprecisa ser standalone para fazer isso!"),
    ARENA_INVALID_PORTAL("messages/global-messages.yml", "arena.error.invalid-portal", "&cPortal inválido. Use 'red' ou 'blue'."),
    ARENA_HEIGHT_LIMIT_OUT_OF_BOUNDS("messages/global-messages.yml", "arena.error.height-limit-out-of-bounds", "&cO limite de altura deve ficar entre 0 e 256!"),
    ARENA_VOID_LEVEL_OUT_OF_BOUNDS("messages/global-messages.yml", "arena.error.void-level-out-of-bounds", "&cO nível do void deve ficar entre 0 e 256!"),
    ARENA_FFA_ARENAS_NO_SPAWNS("messages/global-messages.yml", "arena.error.ffa-arenas-no-spawns", "&cFFA Arenas do not need spawn positions. Use &4/ffa setspawn&c."),
    ARENA_CANNOT_TOGGLE_FFA("messages/global-messages.yml", "arena.error.cannot-toggle-ffa", "&cVocê não pode ativar ou desativar arenas de FFA!"),
    ARENA_CANNOT_ADD_KITS_TO_FFA("messages/global-messages.yml", "arena.error.cannot-add-kits-to-ffa", "&cVocê não pode adicionar kits a arenas de FFA!"),
    ARENA_SPAWN_NOT_SET("messages/global-messages.yml", "arena.error.spawn-not-set", "&cVocê precisa definir as duas posições de spawn! (/arena setspawn)"),
    ARENA_CENTER_NOT_SET("messages/global-messages.yml", "arena.error.center-not-set", "&cVocê precisa definir o centro! (/arena setcenter)"),
    ARENA_MUST_ADD_KIT("messages/global-messages.yml", "arena.error.must-add-kit", "&cVocê precisa adicionar pelo menos um kit! (/arena addkit)"),
    ARENA_ASSIGNED_KIT_NULL("messages/global-messages.yml", "arena.error.assigned-kit-null", "&cO kit &6{kit-name} &cestá atribuído a esta arena, mas não existe!"),
    ARENA_STANDALONE_PORTALS_NOT_SET("messages/global-messages.yml", "arena.error.standalone-portals-not-set", "&cVocê precisa definir os dois portais de time! (/arena setportal)"),
    ARENA_IS_NOT_FFA("messages/global-messages.yml", "arena.error.is-not-ffa", "&cIsto não é uma arena de FFA!"),
    ARENA_INVALID_SPAWN_TYPE("messages/global-messages.yml", "arena.error.invalid-spawn-type", "&cTipo de spawn inválido! Tipos válidos: blue, red, ffa"),

    ARENA_SELECTION_TOOL_ADDED("messages/global-messages.yml", "arena.command.selection-tool.added", "&aFerramenta de seleção adicionada ao seu inventário com sucesso!"),
    ARENA_SELECTION_TOOL_REMOVED("messages/global-messages.yml", "arena.command.selection-tool.removed", "&cFerramenta de seleção removida do seu inventário com sucesso!"),
    ARENA_SELECTED_BOUNDARY("messages/global-messages.yml", "arena.command.selected-boundary", "&aLimite &6{boundary-type} &adefinido para &6{x}, &6{y}, &6{z}&a!"),
    ARENA_DISPLAY_NAME_SET("messages/global-messages.yml", "arena.command.displayname-set", "&aDefiniu o nome de exibição da arena &6{arena-name} &apara &r{display-name}&a!"),
    ARENA_CENTER_SET("messages/global-messages.yml", "arena.command.center-set", "&aDefiniu o centro da arena &6{arena-name}&a!"),
    ARENA_CUBOID_SET("messages/global-messages.yml", "arena.command.cuboid-set", "&aDefiniu a área da arena &6{arena-name}&a!"),
    ARENA_HEIGHT_LIMIT_SET("messages/global-messages.yml", "arena.command.height-limit-set", "&aDefiniu o limite de altura da arena &6{arena-name}&a para &6{height-limit}&a!"),
    ARENA_PORTAL_SET("messages/global-messages.yml", "arena.command.portal-set", "&aDefiniu o portal &b{portal} &ada arena &6{arena-name}&a!"),
    ARENA_VOID_LEVEL_SET("messages/global-messages.yml", "arena.command.void-level-set", "&aDefiniu o nível do void da arena &6{arena-name}&a para &6{void-level}&a!"),
    ARENA_TOGGLED("messages/global-messages.yml", "arena.command.toggled", "&aArena &6{arena-name} &aalterada para &6{status}&a!"),
    ARENA_SPAWN_SET("messages/global-messages.yml", "arena.command.spawn-set", "&aDefiniu o spawn &6{position} &fda arena &6{arena-name}&a!"),
    ARENA_FFA_SPAWN_SET("messages/global-messages.yml", "arena.command.ffa-spawn-set", "&aDefiniu o spawn de FFA da arena &6{arena-name}&a!"),
    ARENA_KIT_ADDED("messages/global-messages.yml", "arena.command.kit-added", "&aAdicionou o kit &6{kit-name}&a à arena &6{arena-name}&a!"),
    ARENA_KIT_REMOVED("messages/global-messages.yml", "arena.command.kit-removed", "&cRemoveu o kit &6{kit-name}&c da arena &6{arena-name}&c!"),
    ARENA_CREATED("messages/global-messages.yml", "arena.command.created", "&aCriou uma nova arena chamada &6{arena-name}&a com o tipo &6{arena-type}&a!"),
    ARENA_DELETED("messages/global-messages.yml", "arena.command.deleted", "&cRemoveu a arena chamada &6{arena-name}&c!"),
    ARENA_SAVED("messages/global-messages.yml", "arena.command.saved", "&aSalvou a arena &6{arena-name}&a!"),
    ARENA_SAVED_ALL("messages/global-messages.yml", "arena.command.saved-all", "&aSalvou todas as arenas!"),

    COOLDOWN_NOT_FOUND("messages/global-messages.yml", "cooldown.error.not-found", "&cNenhum cooldown encontrado para &6{player-name} &cdo tipo &6{cooldown-type}&c."),
    COOLDOWN_RESET("messages/global-messages.yml", "cooldown.command.reset", "&aCooldown de &6{player-name} &ado tipo &6{cooldown-type} &afoi resetado."),

    COOLDOWN_PEARL_MUST_WAIT("messages/global-messages.yml", "cooldown.pearl.must-wait", "&cVocê precisa esperar &6{time} &cantes de jogar outra ender pearl!"),
    COOLDOWN_CAN_NOW_USE_PEARLS_AGAIN("messages/global-messages.yml", "cooldown.pearl.can-now-use-pearls-again", "&aVocê já pode usar ender pearls novamente!"),
    COOLDOWN_CANNOT_USE_AT_FFA_SPAWN("messages/global-messages.yml", "cooldown.pearl.cannot-use-at-ffa-spawn", "&cVocê não pode usar ender pearls enquanto estiver no spawn do FFA!"),

    COOLDOWN_FIREBALL_MUST_WAIT("messages/global-messages.yml", "cooldown.fireball.must-wait", "&cVocê precisa esperar &6{time} &cantes de jogar outra fireball!"),
    COOLDOWN_PARTY_ANNOUNCE_MUST_WAIT("messages/global-messages.yml", "cooldown.party-announce.must-wait", "&cVocê precisa esperar &6{time} &cantes de anunciar sua party novamente!"),
    COOLDOWN_GOLDEN_HEAD_MUST_WAIT("messages/global-messages.yml", "cooldown.golden-head.must-wait", "&cVocê precisa esperar &6{time} &cantes de comer outra golden head!"),

    COSMETIC_NOT_OWNED("messages/global-messages.yml", "cosmetics.not-owned", "&cVocê não possui o cosmético &6{cosmetic-name}&c!"),
    COSMETIC_SELECTED("messages/global-messages.yml", "cosmetics.selected", "&aSelecionou o cosmético &6{cosmetic-name}&a!"),
    COSMETIC_ALREADY_SELECTED("messages/global-messages.yml", "cosmetics.already-selected", "&cVocê já selecionou o cosmético &6{cosmetic-name}&c!"),

    COSMETIC_SET_FOR_PLAYER("messages/global-messages.yml", "cosmetics.command.set", "&aDefiniu &6{cosmetic} &a{type} como cosmético ativo de &6{player}&a!"),
    COSMETICS_NONE_REGISTERED("messages/global-messages.yml", "cosmetics.error.none-registered", "&cNão há cosméticos registrados no servidor!"),
    COSMETIC_TYPE_NOT_SUPPORTED("messages/global-messages.yml", "cosmetics.error.type-not-supported", "&cO tipo de cosmético &6{type} &cnão é suportado!"),
    COSMETIC_NOT_FOUND("messages/global-messages.yml", "cosmetics.error.not-found", "&cNão existe um cosmético chamado &6{input}&c!"),

    COSMETIC_ALREADY_OWNED("messages/global-messages.yml", "cosmetics.error.already-owner", "&cVocê já possui este cosmético."),
    COSMETIC_PURCHASE_SUCCESS("messages/global-messages.yml", "cosmetics.purchase-success", "&aVocê comprou o cosmético &6{cosmetic}&a!"),
    COSMETIC_PURCHASE_INSUFFICIENT_FUNDS("messages/global-messages.yml", "cosmetics.error.purchase-insufficient-funds", "&cVocê não tem moedas suficientes para comprar isto."),

    CHAT_CHANNEL_NOT_EXIST("messages/global-messages.yml", "chat.error.channel-not-exist", "&cO canal de chat &6{channel} &cnão existe."),
    CHAT_CHANNEL_SET("messages/global-messages.yml", "chat.command.channel-set", "&aSeu canal de chat foi definido para &6{channel}&a."),
    CHAT_CHANNEL_ALREADY_IN("messages/global-messages.yml", "chat.error.already-in-channel", "&cVocê já está no canal de chat &6{channel}&c."),

    CHAT_CLEARED_BY_STAFF("messages/global-messages.yml", "chat.cleared-by-staff", "&c&lO CHAT FOI LIMPO PELA STAFF!"),

    CRAFTING_TOGGLED("messages/global-messages.yml", "crafting-operations.command.toggled", "&aAs operações de crafting para &6{item} &aagora estão &6{status}&a."),
    CRAFTING_MUST_HOLD_CRAFTABLE_ITEM("messages/global-messages.yml", "crafting-operations.error.must-hold-craftable-item", "&cVocê precisa estar segurando um item craftável para gerenciar o crafting."),

    DIVISION_NOT_FOUND("messages/global-messages.yml", "division.error.not-found", "&cNão existe uma divisão chamada &6{division-name}&c!"),
    DIVISION_TIER_NOT_FOUND("messages/global-messages.yml", "division.error.tier-not-found", "&cNão existe um tier chamado &6{tier-name} &cna divisão &6{division-name}&c!"),
    DIVISION_ALREADY_EXISTS("messages/global-messages.yml", "division.error.already-exists", "&cO nome &6{division-name} &cjá está sendo usado por outra divisão!"),

    DIVISION_DESCRIPTION_SET("messages/global-messages.yml", "division.command.description-set", "&aDefiniu a descrição da divisão &6{division-name} &apara &r{description}&a!"),
    DIVISION_DISPLAY_NAME_SET("messages/global-messages.yml", "division.command.display-name-set", "&aDefiniu o nome de exibição da divisão &6{division-name} &apara &r{display-name}&a!"),
    DIVISION_ICON_SET("messages/global-messages.yml", "division.command.icon-set", "&aDefiniu o ícone da divisão &6{division-name} &apara &6{item-type}:{item-durability}&a!"),
    DIVISION_WINS_SET("messages/global-messages.yml", "division.command.wins-set", "&aDefiniu as vitórias necessárias do tier &6{tier-name} &ada divisão &6{division-name} &apara &6{required-wins}&a!"),
    DIVISION_CREATED("messages/global-messages.yml", "division.command.created", "&aCriou uma nova divisão chamada &6{division-name} &acom &6{required-wins} &avitórias!"),
    DIVISION_DELETED("messages/global-messages.yml", "division.command.deleted", "&cRemoveu a divisão chamada &6{division-name}&c!"),

    EXPLOSIVE_SETTING_UPDATED("messages/global-messages.yml", "explosive.command.setting-updated", "&aDefiniu o valor de {setting-name} dos explosivos para &6{setting-value}&a."),

    ERROR_AMOUNT_MUST_BE_GREATER_THAN_ZERO("messages/global-messages.yml", "error-messages.amount-must-be-greater-than-zero", "&cA quantidade precisa ser maior que zero!"),

    ERROR_DUEL_REQUESTS_EXPIRED("messages/global-messages.yml", "error-messages.duel-requests.error.expired", "&cEsse pedido de duelo expirou."),
    ERROR_DUEL_REQUESTS_NO_ARENA("messages/global-messages.yml", "error-messages.duel-requests.error.no-arenas", "&cNão há arena disponível para esse kit."),
    ERROR_DUEL_REQUESTS_CANT_DUEL_SELF("messages/global-messages.yml", "error-messages.duel-requests.error.cant-duel-self", "&cVocê não pode se duelar!"),
    ERROR_DUEL_REQUESTS_ALREADY_PENDING("messages/global-messages.yml", "error-messages.duel-requests.error.already-pending", "&cVocê já tem um pedido de duelo pendente desse jogador."),
    ERROR_DUEL_REQUESTS_ALREADY_PENDING_PARTY("messages/global-messages.yml", "error-messages.duel-requests.error.already-pending-party", "&cVocê já tem um pedido de duelo pendente desse jogador ou da party dele."),
    ERROR_DUEL_REQUESTS_INVALID_FROM_PLAYER("messages/global-messages.yml", "error-messages.duel-requests.error.no-pending-request", "&cVocê não possui um pedido de duelo pendente desse jogador."),
    ERROR_DUEL_REQUESTS_REQUESTS_DISABLED_PLAYER("messages/global-messages.yml", "error-messages.duel-requests.error.player-requests-disabled", "&c{name-color}{player} está com pedidos de duelo desativados."),

    ERROR_INVALID_PAGE_NUMBER("messages/global-messages.yml", "error-messages.invalid-page-number", "&c'{input}' não é um número de página válido! Digite um número válido."),
    ERROR_INVALID_NUMBER("messages/global-messages.yml", "error-messages.invalid.number", "&c'{input}' não é um número válido! Digite um número válido."),
    ERROR_INVALID_PLAYER("messages/global-messages.yml", "error-messages.invalid.player", "&cEsse jogador não foi encontrado!"),
    ERROR_INVALID_TYPE("messages/global-messages.yml", "error-messages.invalid.type", "&c{type} inválido. Tipos disponíveis: &6{types}&c."),
    ERROR_INVALID_ITEM("messages/global-messages.yml", "error-messages.invalid.item", "&cItem inválido!"),
    ERROR_INVALID_BOOLEAN("messages/global-messages.yml", "error-messages.invalid-parameters", "&cParâmetros inválidos! Use true ou false."),
    ERROR_NO_MORE_PAGES_AVAILABLE("messages/global-messages.yml", "error-messages.no-more-pages-available", "&c{input} não é um número de página válido! Existem apenas &6{max-pages} &cpáginas disponíveis."),

    ERROR_PLAYER_IS_BUSY("messages/global-messages.yml", "error-messages.player.is-busy", "&6{name-color}{player} &cestá ocupado."),
    ERROR_PLAYER_NOT_PLAYING_MATCH("messages/global-messages.yml", "error-messages.player.not-playing-match", "&c{name-color}{player} &cnão está em uma partida."),
    ERROR_PLAYER_NOT_PLAYING_FFA("messages/global-messages.yml", "error-messages.player.not-playing-ffa", "&c{name-color}{player} &cnão está em uma partida de FFA!"),
    ERROR_PLAYER_PARTY_INVITES_DISABLED("messages/global-messages.yml", "error-messages.player.party-invites-disabled", "&c{name-color}{player} &cestá com convites de party desativados."),

    ERROR_MUST_SELECT_MUSIC("messages/global-messages.yml", "profile-messages.error.must-select-music-before-toggle", "&cVocê precisa selecionar pelo menos um disco antes de ativar a música!"),
    ERROR_YOU_MUST_LEAVE_PARTY("messages/global-messages.yml", "error-messages.you.must-leave-party", "&cVocê precisa sair da sua party antes de fazer isso!"),
    ERROR_YOU_MUST_BE_IN_LOBBY("messages/global-messages.yml", "error-messages.you.must-be-in-lobby", "&cVocê precisa estar no lobby para fazer isso!"),
    ERROR_YOU_MUST_HOLD_ITEM("messages/global-messages.yml", "error-messages.you.must-hold-item", "&cVocê precisa estar segurando um item!"),
    ERROR_YOU_NOT_PLAYING_FFA("messages/global-messages.yml", "error-messages.you.not-playing-ffa", "&cVocê não está em uma partida de FFA!"),
    ERROR_YOU_NOT_SPECTATING_FFA("messages/global-messages.yml", "error-messages.you.not-spectating-ffa", "&cVocê não está assistindo a uma partida de FFA!"),
    ERROR_YOU_ALREADY_SPECTATING_FFA("messages/global-messages.yml", "error-messages.you.already-spectating-ffa", "&cVocê já está assistindo ao FFA!"),
    ERROR_YOU_NOT_PLAYING_MATCH("messages/global-messages.yml", "error-messages.you.not-playing-match", "&cVocê não está em uma partida!"),
    ERROR_YOU_ALREADY_PLAYING_MATCH("messages/global-messages.yml", "error-messages.you.already-playing-match", "&cVocê já está em uma partida!"),
    ERROR_YOU_NOT_SPECTATING_MATCH("messages/global-messages.yml", "error-messages.you.not-spectating-match", "&cVocê não está assistindo a uma partida!"),
    ERROR_YOU_ALREADY_SPECTATING_MATCH("messages/global-messages.yml", "error-messages.you.already-spectating-match", "&cVocê já está assistindo a uma partida!"),
    ERROR_YOU_NO_MATCH_HISTORY("messages/global-messages.yml", "error-messages.you.no-match-history", "&cVocê não tem histórico de partidas."),
    ERROR_YOU_PARTY_NOT_PUBLIC("messages/global-messages.yml", "error-messages.you.party-not-public", "&cSua party não está pública para anunciar. Use: &7/party open"),

    ERROR_YOU_NOT_IN_PARTY("messages/global-messages.yml", "error-messages.you..not-in-party", "&cVocê não está em uma party."),
    ERROR_YOU_NOT_PARTY_LEADER("messages/global-messages.yml", "error-messages.you.not-party-leader", "&cVocê não é o líder da party."),
    ERROR_YOU_ALREADY_IN_PARTY("messages/global-messages.yml", "error-messages.you.already-in-party", "&cVocê já está em uma party."),
    ERROR_YOU_ALREADY_IN_THIS_PARTY("messages/global-messages.yml", "error-messages.you.already-in-this-party", "&cVocê já está nesta party."),
    ERROR_YOU_PARTY_CHAT_DISABLED("messages/global-messages.yml", "error-messages.you.party-chat-disabled", "&cVocê está com as mensagens da party desativadas. &7(Para ativar: /togglepartymessages)"),
    ERROR_YOU_NO_PARTY_INVITE_FROM_PLAYER("messages/global-messages.yml", "error-messages.you.no-party-invite-from-player", "&cVocê não possui convite de party de &6{name-color}{player}&c."),
    ERROR_YOU_PARTY_NEED_TWO_PLAYERS("messages/global-messages.yml", "error-messages.you.party-need-two-players", "&cVocê precisa de pelo menos dois jogadores."),
    ERROR_YOU_ARE_IN_COMBAT("messages/global-messages.yml", "error-messages.you.are-in-combat", "&cVocê está em combate!"),

    ERROR_YOU_BANNED_FROM_PARTY("messages/global-messages.yml", "error-messages.you.banned-from-party", "&cVocê está banido da party de &6{name-color}{player}&c."),

    FFA_ADDED_PLAYER("messages/global-messages.yml", "ffa.added-player", "&a&lADICIONADO! &6{name-color}{player} &7&l» &6FFA {ffa-name}"),
    FFA_KICKED_PLAYER("messages/global-messages.yml", "ffa.kicked-player", "&c&lEXPULSO! &6{name-color}{player} &7&l» &6FFA {ffa-name}"),
    FFA_SPAWN_ENTERED("messages/global-messages.yml", "ffa.entered-spawn", "&aVocê entrou no spawn do FFA."),
    FFA_SPAWN_LEFT("messages/global-messages.yml", "ffa.left-spawn", "&aVocê saiu do spawn do FFA."),

    FFA_ALREADY_EXISTS("messages/global-messages.yml", "ffa.error.already-exists", "&cUma partida de FFA chamada &6{ffa-name} &cjá existe!"),
    FFA_NOT_FOUND("messages/global-messages.yml", "ffa.error.not-found", "&cUma partida de FFA chamada &6{ffa-name} &cnão existe!"),
    FFA_DISABLED("messages/global-messages.yml", "ffa.error.disabled", "&cO modo FFA está desativado para o kit &6{kit-name}&c!"),
    FFA_KIT_NOT_ELIGIBLE("messages/global-messages.yml", "ffa.error.kit-not-eligible", "&cEste kit não é elegível para partidas de FFA! Desative as &6CONFIGURAÇÕES DE BUILD/BOXING &ce tente novamente."),
    FFA_CAN_ONLY_SETUP_IN_FFA_ARENA("messages/global-messages.yml", "ffa.error.can-only-setup-in-ffa-arena", "&cVocê só pode configurar partidas de FFA em arenas FFA!"),
    FFA_FULL("messages/global-messages.yml", "ffa.error.ffa-full", "&cEsta partida de FFA está lotada!"),
    FFA_INVALID_SPAWN_TYPE("messages/global-messages.yml", "ffa.error.invalid-spawn-type", "&cTipo de spawn inválido! Tipos válidos: pos1, pos2"),

    FFA_MATCH_CREATED("messages/global-messages.yml", "ffa.command.created", "&aCriou uma nova partida de FFA para o kit &6{kit-name}&a!"),
    FFA_TOGGLED("messages/global-messages.yml", "ffa.command.toggled", "&aO modo FFA foi &6{status} &apara o kit &6{kit-name}&a!"),
    FFA_ARENA_SET("messages/global-messages.yml", "ffa.command.arena-set", "&aDefiniu a arena da partida de FFA do kit &6{kit-name} &apara &6{arena-name}&a!"),
    FFA_SAFE_ZONE_SET("messages/global-messages.yml", "ffa.command.safe-zone-set", "&aDefiniu a safezone &6{pos} &ada partida de FFA do kit &6{kit-name}&a!"),
    FFA_MAX_PLAYERS_SET("messages/global-messages.yml", "ffa.command.max-players-set", "&aDefiniu o máximo de jogadores da partida de FFA do kit &6{kit-name} &apara &6{max-players}&a!"),
    FFA_SPAWN_SET("messages/global-messages.yml", "ffa.command.spawn-set", "&aA posição de spawn do FFA foi definida para &6{arena-name}&a!"),

    FFA_KITS_RELOADED("messages/global-messages.yml", "ffa.command.kits-reloaded", "&aRecarregou todos os kits de FFA!"),

    HOTBAR_NOT_FOUND("messages/global-messages.yml", "hotbar.error.not-found", "&cO item da hotbar chamado &e{hotbar-name} &cnão existe."),
    HOTBAR_NO_HOTBAR_ITEMS_CREATED("messages/global-messages.yml", "hotbar.error.no-hotbar-items-created", "&cNenhum item de hotbar foi criado ainda."),

    HOTBAR_CREATED_ITEM("messages/global-messages.yml", "hotbar.command.created", "&aVocê criou um novo item de hotbar chamado &e{hotbar-name}&a."),
    HOTBAR_DELETED_ITEM("messages/global-messages.yml", "hotbar.command.deleted", "&aVocê removeu o item de hotbar chamado &e{hotbar-name}&a."),

    ITEM_GIVEN("messages/global-messages.yml", "item.command.given-item", "&aVocê recebeu &6{amount} &a{item-name}&a."),
    ITEM_NOT_CONFIGURED("messages/global-messages.yml", "item.error.item-not-configured", "&cO item chamado &6{item-name} &cnão está configurado."),

    ITEM_ENCHANTED("messages/global-messages.yml", "item.command.enchanted-item", "&aVocê encantou seu &6{item-name} &acom &6{enchantment} &anível &6{level}&a."),

    JOIN_MESSAGE_CHAT_ENABLED("messages/global-messages.yml", "join-message.enabled", true),
    JOIN_MESSAGE_CHAT_MESSAGE_LIST("messages/global-messages.yml", "join-message.message", Arrays.asList(
            "",
            "&6&lKaosPractice Core",
            " &6&l│ &rWebsite: &6kaosmc.com",
            " &6&l│ &rDiscord: &6discord.gg/kaosmc",
            "",
            "&6&lCriado por &f{author} &7(v{version})",
            ""
    )),

    KIT_NOT_FOUND("messages/global-messages.yml", "kit.error.not-found", "&cNão existe kit com esse nome!"), //TODO: add {kit-name} placeholder
    KIT_ALREADY_EXISTS("messages/global-messages.yml", "kit.error.already-exists", "&cJá existe um kit com esse nome!"), //TODO: add {kit-name} placeholder
    KIT_SLOT_MUST_BE_NUMBER("messages/global-messages.yml", "kit.error.slot-must-be-number", "&cO slot precisa ser um número!"),

    KIT_POTION_EFFECT_REMOVED("messages/global-messages.yml", "kit.command.potion-effect-removed", "&cVocê removeu o efeito de poção &6{potion-effect} &cdo kit &6{kit-name}&c."),
    KIT_RAIDING_ROLE_KIT_REMOVED("messages/global-messages.yml", "kit.command.raiding-role-kit-removed", "&aRemoveu o kit de papel de raiding &6{role} &ade &6{role-kit-name}&a para o kit &6{kit-name}&a."),

    KIT_INVENTORY_GIVEN("messages/global-messages.yml", "kit.command.inventory-given", "&aRecuperou o inventário do kit &6{kit-name}&a!"),
    KIT_INVENTORY_SET("messages/global-messages.yml", "kit.command.inventory-set", "&aDefiniu o inventário do kit &6{kit-name}&a!"),
    KIT_FFA_SLOT_SET("messages/global-messages.yml", "kit.command.ffaslot-set", "&aDefiniu o slot de FFA do kit &6{kit-name} &apara &6{slot}&a!"),
    KIT_DESCRIPTION_SET("messages/global-messages.yml", "kit.command.description-set", "&aDefiniu a descrição do kit &6{kit-name}&a: &r{description}"),
    KIT_DESCRIPTION_CLEARED("messages/global-messages.yml", "kit.command.description-cleared", "&aLimpou a descrição do kit &6{kit-name}&a!"),
    KIT_DISCLAIMER_SET("messages/global-messages.yml", "kit.command.disclaimer-set", "&aDefiniu o aviso do kit &6{kit-name}&a: &r{disclaimer}"),
    KIT_DISPLAYNAME_SET("messages/global-messages.yml", "kit.command.displayname-set", "&aDefiniu o nome de exibição do kit &6{kit-name}&a: &r{display-name}"),
    KIT_ICON_SET("messages/global-messages.yml", "kit.command.icon-set", "&aDefiniu o ícone do kit &6{kit-name} &apara &6{icon}&a!"),
    KIT_CATEGORY_SET("messages/global-messages.yml", "kit.command.category-set", "&aDefiniu a categoria do kit &6{kit-name} &apara &6{category}&a!"),
    KIT_MENU_TITLE_SET("messages/global-messages.yml", "kit.command.menu-title-set", "&aDefiniu o título do menu do kit &6{kit-name}&a: &r{title}"),
    KIT_KB_PROFILE_SET("messages/global-messages.yml", "kit.command.kb-profile-set", "&aDefiniu o perfil de knockback do kit &6{kit-name} &apara &6{kb-profile}&a!"),
    KIT_SET_EDITABLE("messages/global-messages.yml", "kit.command.set-editable", "&aDefiniu o estado editável do kit &6{kit-name} &apara &6{editable}&a!"),
    KIT_SET_RAIDING_ROLE_KIT("messages/global-messages.yml", "kit.command.set-raiding-role-kit", "&aDefiniu o kit de papel de raiding &6{role} &apara &6{role-kit} &ano kit &6{kit-name}&a."),
    KIT_CANNOT_SET_ENABLED_AS_RAIDING_ROLE_KIT("messages/global-messages.yml", "kit.error.cannot-set-enabled-as-raiding-role-kit", "&cO kit &6{role-kit} &cestá ativado. Desative-o antes de defini-lo como kit de papel de raiding."),
    KIT_NEED_TO_DISABLE_TO_SET_RAIDING_ROLE("messages/global-messages.yml", "kit.error.need-to-disable-to-set-raiding-role-kit", "&cVocê precisa desativar o kit &6{kit-name} &cantes de definir um kit de papel de raiding."),
    KIT_RAIDING_ROLE_KIT_NOT_MAPPED("messages/global-messages.yml", "kit.error.raiding-role-kit-not-present", "&cO kit &6{kit-name} &cnão possui um kit de raiding mapeado para o papel &6{role}&c."),

    KIT_SETTING_NOT_ENABLED("messages/global-messages.yml", "kit.error.setting-not-enabled", "&cA configuração &6{setting-name} &cnão está ativada para o kit &6{kit-name}&c."),

    KIT_POTION_EFFECTS_SET("messages/global-messages.yml", "kit.command.potion-effects-set", "&aDefiniu os efeitos de poção do kit &6{kit-name}&a!"),
    KIT_POTION_EFFECTS_CLEARED("messages/global-messages.yml", "kit.command.potion-effects-cleared", "&aLimpou os efeitos de poção do kit &6{kit-name}&a!"),
    KIT_CREATED("messages/global-messages.yml", "kit.command.created", "&aCriou um novo kit chamado &6{kit-name}&a!"),
    KIT_DELETED("messages/global-messages.yml", "kit.command.deleted", "&cRemoveu o kit chamado &6{kit-name}&c!"),
    KIT_SETTING_SET("messages/global-messages.yml", "kit.command.setting-set", "&aDefiniu a configuração &6{setting-name} &apara &6{enabled} &ano kit &6{kit-name}&a."),
    KIT_SAVED("messages/global-messages.yml", "kit.command.saved", "&aSalvou o kit &6{kit-name}&a!"),
    KIT_SAVED_ALL("messages/global-messages.yml", "kit.command.saved-all", "&aSalvou todos os kits!"),

    LEVEL_NOT_FOUND("messages/global-messages.yml", "level.error.not-found", "&cNão existe um nível chamado &6{level-name}&c!"),
    LEVEL_ALREADY_EXISTS("messages/global-messages.yml", "level.error.already-exists", "&cJá existe um nível chamado &6{level-name}&c!"),
    LEVEL_MAX_ELO_MUST_BE_GREATER_THAN_MIN("messages/global-messages.yml", "level.error.max-elo-must-be-greater-than-min", "&cO Elo máximo precisa ser maior que o Elo mínimo! (Mínimo: &6{min-elo}&c)"),
    LEVEL_MINIMUM_ELO_CANNOT_BE_NEGATIVE("messages/global-messages.yml", "level.error.min-elo-cannot-be-negative", "&cO Elo mínimo não pode ser negativo!"),
    LEVEL_MINIMUM_ELO_MUST_BE_LESS_THAN_MAXIMUM("messages/global-messages.yml", "level.error.min-elo-must-be-less-than-maximum", "&cO Elo mínimo precisa ser menor que o Elo máximo! (Máximo: &6{max-elo}&c)"),

    LEVEL_DISPLAY_NAME_SET("messages/global-messages.yml", "level.command.display-name-set", "&aDefiniu o nome de exibição do nível &6{level-name} &apara &r{display-name}&a!"),
    LEVEL_ICON_SET("messages/global-messages.yml", "level.command.icon-set", "&aDefiniu o ícone do nível &6{level-name} &apara &6{icon-material}&a!"),
    LEVEL_MINIMUM_ELO_SET("messages/global-messages.yml", "level.command.min-elo-set", "&aDefiniu o Elo mínimo do nível &6{level-name} &apara &6{min-elo}&a!"),
    LEVEL_MAX_ELO_SET("messages/global-messages.yml", "level.command.max-elo-set", "&aDefiniu o Elo máximo do nível &6{level-name} &apara &6{max-elo}&a!"),
    LEVEL_CREATED("messages/global-messages.yml", "level.command.created", "&aCriou um novo nível chamado &6{level-name} &acom Elo mínimo &6{min-elo} &ae Elo máximo &6{max-elo}&a!"),
    LEVEL_DELETED("messages/global-messages.yml", "level.command.deleted", "&cRemoveu o nível chamado &6{level-name}&c!"),

    MATCH_CANCELLED_FOR_PLAYER("messages/global-messages.yml", "match.cancelled", "&aVocê encerrou a partida de &6{name-color}{player}&a."),
    MATCH_COMMAND_BLOCKED("messages/global-messages.yml", "match.error.command-blocked", "&cVocê não pode usar esse comando durante uma partida!"),

    MUSIC_DISC_DESELECTED("messages/global-messages.yml", "music-disc.deselected", "&cVocê removeu &6{disc} &cda sua seleção de músicas."),
    MUSIC_DISC_SELECTED("messages/global-messages.yml", "music-disc.selected", "&aVocê adicionou &6{disc} &à sua seleção de músicas."),
    MUSIC_DISC_NOW_PLAYING("messages/global-messages.yml", "music-disc.now-playing", Collections.singletonList("&7[&6♬&7] &fTocando agora: &6{disc} &7({duration})")),

    OTHER_SUDO_ALL_PLAYERS("messages/global-messages.yml", "other.sudo.all-players", "&aVocê forçou todos os jogadores a dizer: &r{message}"),

    PARTY_DISBANDED("messages/global-messages.yml", "party.disbanded", "&6&lParty &7&l" + Symbol.ARROW_R + " &6{name-color}{player} &cdesfez a party."),
    PARTY_YOU_JOINED("messages/global-messages.yml", "party.joined",
            Arrays.asList(
                    "",
                    "&6&lEntrou na Party &a" + Symbol.TICK,
                    " &7Você entrou na party de &6{name-color}{leader}&a.",
                    " &7Digite /p para ajuda.",
                    ""
            )
    ),
    PARTY_YOU_LEFT("messages/global-messages.yml", "party.left", "&cVocê saiu da party!"),

    PARTY_PLAYER_JOINED("messages/global-messages.yml", "party.player-joined", "&6{name-color}{player} &aentrou na party! &7({current-size}/{max-size})"),
    PARTY_PLAYER_LEFT("messages/global-messages.yml", "party.player-left", "&6{name-color}{player} &csaiu da party. &7({current-size}/{max-size})"),

    PARTY_PLAYER_KICKED("messages/global-messages.yml", "party.player-kicked", "&6{name-color}{player} &cfoi expulso da party. &7({current-size}/{max-size})"),
    PARTY_PLAYER_BANNED("messages/global-messages.yml", "party.player-banned", "&6{name-color}{player} &cfoi banido da party. &7({current-size}/{max-size})"),

    PARTY_CREATED("messages/global-messages.yml", "party.created",
            Arrays.asList(
                    "",
                    "&6&lParty Criada &a" + Symbol.TICK,
                    " &7Digite /p para ajuda.",
                    ""
            )
    ),
    PARTY_LOOKUP("messages/global-messages.yml", "party.lookup", Arrays.asList(
            "",
            " &6&lParty de {leader}",
            "  &6&l│ &rLíder: &6{name-color}{leader}",
            "  &6&l│ &rMembros: &6{members}",
            "  &6&l│ &rStatus: &6{status}",
            "  &6&l│ &rPrivacidade: &6{privacy}",
            ""
    )),
    PARTY_INFO_NO_MEMBERS_FORMAT("messages/global-messages.yml", "party.info.no-members-format", "&cSem Membros"),
    PARTY_INFO("messages/global-messages.yml", "party.info.format", Arrays.asList(
            "",
            " &6&lInformações da Party",
            "  &6&l│ &rLíder: &6{name-color}{leader}",
            "  &6&l│ &rMembros &7({members-amount})&f: &6{members}",
            "  &6&l│ &rPrivacidade: &6{privacy}",
            "  &6&l│ &rTamanho: &6{size}",
            ""
    )),

    PROFILE_TOGGLED_PARTY_INVITES("messages/global-messages.yml", "profile-messages.player-settings.party-invites", "&aVocê {status} &aos convites de party."),
    PROFILE_TOGGLED_PARTY_MESSAGES("messages/global-messages.yml", "profile-messages.player-settings.party-messages", "&aVocê {status} &aas mensagens da party."),
    PROFILE_TOGGLED_SCOREBOARD("messages/global-messages.yml", "profile-messages.player-settings.scoreboard", "&aVocê {status} &aa scoreboard."),
    PROFILE_TOGGLED_SCOREBOARD_LINES("messages/global-messages.yml", "profile-messages.player-settings.scoreboard-lines", "&aVocê {status} &aas linhas da scoreboard."),
    PROFILE_TOGGLED_TAB_LIST("messages/global-messages.yml", "profile-messages.player-settings.tab-list", "&aVocê {status} &aa tablist."),
    PROFILE_TOGGLED_PROFANITY_FILTER("messages/global-messages.yml", "profile-messages.player-settings.profanity-filter", "&aVocê {status} &ao filtro de palavrões."),
    PROFILE_TOGGLED_DUEL_REQUESTS("messages/global-messages.yml", "profile-messages.player-settings.duel-requests", "&aVocê {status} &ao recebimento de pedidos de duelo."),
    PROFILE_TOGGLED_LOBBY_MUSIC("messages/global-messages.yml", "profile-messages.player-settings.lobby-music", "&aVocê {status} &aa música do lobby."),
    PROFILE_TOGGLED_SERVER_TITLES("messages/global-messages.yml", "profile-messages.player-settings.server-titles", "&aVocê {status} &aos títulos do servidor."),
    PROFILE_WORLD_TIME_SET("messages/global-messages.yml", "profile-messages.world-time-set", "&aDefiniu seu horário pessoal do mundo para &6{time}&a."),
    PROFILE_WORLD_TIME_RESET("messages/global-messages.yml", "profile-messages.world-time-reset", "&aSeu horário pessoal do mundo foi resetado para o horário do servidor."),

    QUEUE_TEMPORARILY_DISABLED("messages/global-messages.yml", "queue.error.temporarily-disabled", "&cAs filas estão temporariamente desativadas. Tente novamente mais tarde."),
    QUEUE_TOGGLED("messages/global-messages.yml", "queue.command.toggled", "&aVocê {status} &atemporariamente as filas para todos os jogadores."),
    QUEUE_RELOADED("messages/global-messages.yml", "queue.command.reloaded", "&aRecarregou todas as filas!"),
    QUEUE_FORCED_PLAYER("messages/global-messages.yml", "queue.command.forced-player", "&aForçou &6{player} &aentrar na fila &6{ranked} {kit}&a!"),

    QUEUE_PROGRESSING_UNRANKED_BOOLEAN("messages/global-messages.yml", "queue.joined.progressing.unranked.enabled", true),
    QUEUE_PROGRESSING_UNRANKED("messages/global-messages.yml", "queue.joined.progressing.unranked.format", Arrays.asList(
            "",
            "&6&l{kit}",
            " &6&l│ &fFaixa de Ping: &6{ping-range}",
            "  &7&oProcurando partida...",
            ""
    )),

    QUEUE_PROGRESSING_RANKED_BOOLEAN("messages/global-messages.yml", "queue.joined.progressing.ranked.enabled", true),
    QUEUE_PROGRESSING_RANKED("messages/global-messages.yml", "queue.joined.progressing.ranked.format", Arrays.asList(
            "",
            "&6&l{kit} &6&l" + Symbol.RANKED_STAR + "Ranked",
            " &6&l│ &fFaixa de ELO: &6{min-elo} &7&l" + Symbol.ARROW_R + " &6{max-elo}",
            " &6&l│ &fFaixa de Ping: &6{ping-range}",
            "  &7&oProcurando partida...",
            ""
    )),

    QUEUE_PROGRESSING_RANKED_LIMIT_REACHED_BOOLEAN("messages/global-messages.yml", "queue.joined.progressing.ranked.limit-reached.enabled", true),
    QUEUE_PROGRESSING_RANKED_LIMIT_REACHED("messages/global-messages.yml", "queue.joined.progressing.ranked.limit-reached.format", Arrays.asList(
            "",
            "&6&l{kit} &6&l" + Symbol.RANKED_STAR + "Ranked",
            " &6&l│ &fFaixa de Ping: &6{ping-range}",
            "  &c&lLIMITE DE FAIXA ATINGIDO...",
            ""
    )),

    QUEUE_JOINED_BOOLEAN("messages/global-messages.yml", "queue.join-message.enabled", true),
    QUEUE_JOINED("messages/global-messages.yml", "queue.join-message.format", Arrays.asList(
            "",
            "&6&lENTROU NA FILA &a" + Symbol.TICK,
            " &6&l│ &rKit: &6{kit}",
            " &6&l│ &rTipo: &6{queue-type}",
            "  &7&oUse /leavequeue para sair.",
            ""
    )),

    QUEUE_LEFT_BOOLEAN("messages/global-messages.yml", "queue.leave-message.enabled", true),
    QUEUE_LEFT("messages/global-messages.yml", "queue.leave-message.format", Arrays.asList(
            "",
            "&6&lSAIU DA FILA &c" + Symbol.CROSS,
            " &6&l│ &rVocê saiu da fila &6{queue-type} &6{kit}&f.",
            ""
    )),

    RANKED_PLAYER_NOT_BANNED("messages/global-messages.yml", "ranked.error.player-not-banned", "&c{name-color}{player} &cnão está banido das partidas ranked!"),
    RANKED_PLAYER_ALREADY_BANNED("messages/global-messages.yml", "ranked.error.player-already-banned", "&c{name-color}{player} &cjá está banido das partidas ranked!"),

    RANKED_PLAYER_BAN_BROADCAST_BOOLEAN("messages/global-messages.yml", "ranked.ban-broadcast.enabled", true),
    RANKED_PLAYER_BAN_BROADCAST("messages/global-messages.yml", "ranked.ban-broadcast.format", Collections.singletonList("&c&l{name-color}{player} &7foi banido das partidas ranked.")),

    RANKED_BAN_MESSAGE_NOTICE_BOOLEAN("messages/global-messages.yml", "ranked.ban-message-notice.enabled", true),
    RANKED_BAN_MESSAGE_NOTICE("messages/global-messages.yml", "ranked.ban-message-notice.format", Arrays.asList(
            "",
            "&c&lRANKED BANIDO",
            " &c&l│ &fMotivo: &c{reason}",
            " &c&l│ &fDuração: &c{duration}",
            " &c&l│ &fBan ID: &c{ban-id}",
            ""
    )),

    RANKED_PLAYER_UNBAN_BROADCAST_BOOLEAN("messages/global-messages.yml", "ranked.unban-broadcast.enabled", true),
    RANKED_PLAYER_UNBAN_BROADCAST("messages/global-messages.yml", "ranked.unban-broadcast.format", Collections.singletonList("&a&l{name-color}{player} &7foi desbanido das partidas ranked.")),

    RANKED_UNBAN_MESSAGE_NOTICE_BOOLEAN("messages/global-messages.yml", "ranked.unban-message-notice.enabled", true),
    RANKED_UNBAN_MESSAGE_NOTICE("messages/global-messages.yml", "ranked.unban-message-notice.format", Arrays.asList(
            "",
            "&a&lRANKED DESBANIDO",
            " &a&l│ &fMotivo: &a{reason}",
            " &a&l│ &fBan ID: &a{ban-id}",
            " &a(Você já pode entrar na fila ranked novamente)",
            ""
    )),

    SPAWN_SET("messages/global-messages.yml", "spawn.command.set", "&aLocal de spawn definido com sucesso para &6KaosPractice&a! \n &8- &7{world}: {x}, {y}, {z} (Yaw: {yaw}, Pitch: {pitch})"),
    SPAWN_TELEPORTED("messages/global-messages.yml", "spawn.command.teleported", "&6Voce foi teleportado para o spawn!"),
    SPAWN_ITEMS_GIVEN("messages/global-messages.yml", "spawn.command.items-given", "&aItens de spawn recebidos com sucesso!"),

    SNAPSHOT_INVENTORY_EXPIRED("messages/global-messages.yml", "snapshot.error.inventory-expired", "&cEste inventário expirou."),

    TIPS_LIST("messages/global-messages.yml", "tips", Arrays.asList(
            "&6Dica: &fUse F5 para olhar seu oponente uma última vez antes do fim.",
            "&6Dica: &fDê W-tap como se sua vida dependesse disso. E depende.",
            "&6Dica: &fMantenha a mira na altura da cabeça... a menos que você goste de acertar o pé.",
            "&6Dica: &fTreine spacing. Ou só cole no inimigo e reze."
    )),

    TROLL_PLAYER_DONUTED("messages/global-messages.yml", "troll.player-donuted", "&aVocê aplicou o troll de donut em &6{name-color}{player}&a!"),
    TROLL_PLAYER_FAKE_EXPLODED("messages/global-messages.yml", "troll.player-fake-exploded", "&aVocê simulou uma explosão para &6{name-color}{player}&a!"),
    TROLL_PLAYER_GIVEN_HEART_ATTACK("messages/global-messages.yml", "troll.player-given-heart-attack", "&aVocê deu um ataque do coração em &6{name-color}{player}&a!"),
    TROLL_PLAYER_LAUNCHED("messages/global-messages.yml", "troll.player-launched", "&aVocê lançou &6{name-color}{player}&a para o céu!"),
    TROLL_PLAYER_PUSHED("messages/global-messages.yml", "troll.player-pushed", "&aVocê empurrou &6{name-color}{player}&a!"),
    TROLL_PLAYER_STRUCK_BY_LIGHTNING("messages/global-messages.yml", "troll.player-struck-by-lightning", "&aVocê atingiu &6{name-color}{player} &acom um raio!"),
    TROLL_PLAYER_DEMO_MENU_OPENED("messages/global-messages.yml", "troll.player-demo-menu-opened", "&aVocê abriu o menu demo para &6{name-color}{player}&a!"),

    ;

    private final String configName;
    private final String configPath;
    private final Object defaultValue;

    /**
     * Constructor for the MessagesLocale enum.
     *
     * @param configName   The name of the configuration file.
     * @param configPath   The path to the specific string within the configuration file.
     * @param defaultValue The default value for the locale entry.
     */
    GlobalMessagesLocaleImpl(String configName, String configPath, Object defaultValue) {
        this.configName = configName;
        this.configPath = configPath;
        this.defaultValue = defaultValue;
    }
}
