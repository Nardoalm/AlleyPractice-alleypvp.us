package com.kaosmc.practice.core.profile.menu.setting.enums;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.common.text.LoreHelper;
import com.kaosmc.practice.core.profile.data.types.ProfileSettingData;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * @author Emmy
 * @project Kaos
 * @since 21/04/2025
 */
public enum PracticeSettingType {
    PARTY_MESSAGES(10, "&6&lMensagens da Party", Material.FEATHER,
            settings -> Arrays.asList(
                    CC.MENU_BAR,
                    "&7Ver mensagens do chat da party.",
                    "",
                    LoreHelper.displayEnabled(settings.isPartyMessagesEnabled()),
                    "",
                    "&aClique para alternar.",
                    CC.MENU_BAR
            )
    ),

    PARTY_INVITES(11, "&6&lConvites de Party", Material.NAME_TAG,
            settings -> Arrays.asList(
                    CC.MENU_BAR,
                    "&7Receber convites de party.",
                    "",
                    LoreHelper.displayEnabled(settings.isPartyInvitesEnabled()),
                    "",
                    "&aClique para alternar.",
                    CC.MENU_BAR
            )
    ),

    SIDEBAR_VISIBILITY(12, "&6&lVisibilidade da Sidebar", Material.CARPET, 5,
            settings -> Arrays.asList(
                    CC.MENU_BAR,
                    "&7Ver a scoreboard.",
                    "",
                    LoreHelper.displayShown(settings.isScoreboardEnabled()),
                    "",
                    "&aClique para alternar.",
                    CC.MENU_BAR
            )
    ),

    PING_RANGE(19, "&6&lFaixa de Ping", Material.STICK,
            settings -> Arrays.asList(
                    CC.MENU_BAR,
                    "&7Limita o pareamento pela diferença de ping.",
                    "",
                    "&f&l│ &6Atual: &f" + settings.getPingRangeDisplay(),
                    "",
                    "&aClique para alterar.",
                    CC.MENU_BAR
            )
    ),

    WORLD_TIME(13, "&6&lHorário do Mundo", Material.WATCH, settings -> Arrays.asList(
            CC.MENU_BAR,
            "&7Altere o horário do seu mundo.",
            "",
            formatTime("Padrão", settings.isDefaultTime(), "&a&l"),
            formatTime("Dia", settings.isDayTime(), "&e&l"),
            formatTime("Pôr do Sol", settings.isSunsetTime(), "&6&l"),
            formatTime("Noite", settings.isNightTime(), "&4&l"),
            "",
            "&aClique para alternar.",
            CC.MENU_BAR
    )),

    SCOREBOARD_LINES(14, "&6&lLinhas da Scoreboard", Material.STRING,
            settings -> Arrays.asList(
                    CC.MENU_BAR,
                    "&7Exibir linhas da scoreboard.",
                    "",
                    LoreHelper.displayShown(settings.isShowScoreboardLines()),
                    "",
                    "&aClique para alternar.",
                    CC.MENU_BAR
            )
    ),

    DUEL_REQUESTS(21, "&6&lPedidos de Duel", Material.DIAMOND_SWORD,
            settings -> Arrays.asList(
                    CC.MENU_BAR,
                    "&7Receber pedidos de duel.",
                    "",
                    LoreHelper.displayEnabled(settings.isReceiveDuelRequestsEnabled()),
                    "",
                    "&aClique para alternar.",
                    CC.MENU_BAR
            )
    ),
    SERVER_TITLES(20, "&6&lTítulos do Servidor", Material.PAPER,
            settings -> Arrays.asList(
                    CC.MENU_BAR,
                    "&7Exibe títulos enviados pelo servidor.",
                    "",
                    LoreHelper.displayShown(settings.isServerTitles()),
                    "",
                    "&aClique para alternar.",
                    CC.MENU_BAR
            )
    ),

    MATCH_SETTINGS(16, "&6&lConfigurações de Match", Material.BOOK,
            settings -> Arrays.asList(
                    CC.MENU_BAR,
                    "&7Ajuste suas configurações de match.",
                    "",
                    "&aClique para visualizar.",
                    CC.MENU_BAR
            )
    ),

    COSMETICS(25, "&6&lCosméticos", Material.NETHER_STAR,
            settings -> Arrays.asList(
                    CC.MENU_BAR,
                    "&7Personalize seus cosméticos.",
                    "",
                    "&aClique para visualizar.",
                    CC.MENU_BAR
            )
    ),

    LOBBY_MUSIC(34, "&6&lMúsica do Lobby", Material.JUKEBOX,
            settings -> Arrays.asList(
                    CC.MENU_BAR,
                    "&7Personalize sua música do lobby.",
                    "",
                    "&aClique para visualizar.",
                    CC.MENU_BAR
            )
    ),

    ;

    public final int slot;
    public final String displayName;
    public final Material material;
    public final int durability;
    public final Function<ProfileSettingData, List<String>> loreProvider;

    /**
     * Constructor for the EnumPracticeSettingType enum.
     *
     * @param slot         The slot of the item in the menu.
     * @param displayName  The display name of the item.
     * @param material     The material of the item.
     * @param loreProvider A function that provides the lore for the item based on ProfileSettingData.
     */
    PracticeSettingType(int slot, String displayName, Material material, Function<ProfileSettingData, List<String>> loreProvider) {
        this(slot, displayName, material, 0, loreProvider);
    }

    /**
     * Constructor for the EnumPracticeSettingType enum.
     *
     * @param slot         The slot of the item in the menu.
     * @param displayName  The display name of the item.
     * @param material     The material of the item.
     * @param durability   The durability of the item.
     * @param loreProvider A function that provides the lore for the item based on ProfileSettingData.
     */
    PracticeSettingType(int slot, String displayName, Material material, int durability, Function<ProfileSettingData, List<String>> loreProvider) {
        this.slot = slot;
        this.displayName = displayName;
        this.material = material;
        this.durability = durability;
        this.loreProvider = loreProvider;
    }

    /**
     * Formats the time string based on the active status.
     *
     * @param label       The label to display.
     * @param active      Whether the time is active or not.
     * @param activeColor The color for the active state.
     * @return The formatted time string.
     */
    private static String formatTime(String label, boolean active, String activeColor) {
        return " &f● " + (active ? activeColor : "&7") + label;
    }
}
