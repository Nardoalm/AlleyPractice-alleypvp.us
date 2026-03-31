package com.kaosmc.practice.feature.match.menu;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.feature.match.Match;
import com.kaosmc.practice.feature.match.MatchService;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.library.menu.MenuUtil;
import com.kaosmc.practice.library.menu.pagination.PaginatedMenu;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Remi
 * @project Kaos
 * @date 5/26/2024
 */
public class CurrentMatchesMenu extends PaginatedMenu {
    @Override
    public int getSize() {
        return 54;
    }

    /**
     * Gets the title of the menu.
     *
     * @param player the player viewing the menu
     * @return the title of the menu
     */
    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&6&lPartidas Atuais (" + this.getDisplayableMatches().size() + ")";
    }

    /**
     * Gets the buttons to display in the menu.
     *
     * @param player the player viewing the menu
     * @return the buttons to display
     */
    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        final Map<Integer, Button> buttons = new ConcurrentHashMap<>();
        int slot = 0;

        for (Match match : this.getDisplayableMatches()) {
            buttons.put(slot++, new CurrentMatchButton(match));
        }

        return buttons;
    }

    private List<Match> getDisplayableMatches() {
        List<Match> matches = KaosPractice.getInstance().getService(MatchService.class).getMatches();
        List<Match> displayableMatches = new ArrayList<>();

        for (Match match : matches) {
            if (match == null || match.getParticipants() == null || match.getParticipants().size() < 2) {
                continue;
            }

            if (match.getParticipants().get(0) == null || match.getParticipants().get(1) == null) {
                continue;
            }

            displayableMatches.add(match);
        }

        return displayableMatches;
    }

    /**
     * Gets the buttons to display in the global section of the menu.
     *
     * @param player the player viewing the menu
     * @return the global buttons
     */
    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        this.addBorder(buttons, 15, 6);
        buttons.put(4, new RefreshButton());
        buttons.put(49, new CloseButton());
        return buttons;
    }

    @Override
    public int getPreviousPageButtonSlot() {
        return 45;
    }

    @Override
    public int getNextPageButtonSlot() {
        return 53;
    }

    @Override
    public Button getPreviousPageButton(Player player) {
        return MenuUtil.hasPrevious(-1, this) ? new NavigationButton(this, -1) : null;
    }

    @Override
    public Button getNextPageButton(Player player) {
        return MenuUtil.hasNext(player, 1, this) ? new NavigationButton(this, 1) : null;
    }

    @RequiredArgsConstructor
    public static class CurrentMatchButton extends Button {
        private final Match match;

        /**
         * Gets the item stack for the button.
         *
         * @param player the player viewing the button
         * @return the item stack
         */
        @Override
        public ItemStack getButtonItem(Player player) {
            String queueName = match.getQueue() == null || match.getQueue().getQueueType() == null
                    ? "Privada"
                    : String.valueOf(match.getQueue().getQueueType());
            String firstName = resolveParticipantName(0);
            String secondName = resolveParticipantName(1);
            String kitName = match.getKit() == null ? "Desconhecido" : match.getKit().getDisplayName();
            String arenaName = match.getArena() == null ? "Desconhecida" : match.getArena().getName();
            Material icon = match.getKit() == null || match.getKit().getIcon() == null ? Material.PAPER : match.getKit().getIcon();
            short durability = (short) (match.getKit() == null ? 0 : match.getKit().getDurability());

            return new ItemBuilder(icon).name("&6&l" + firstName + " &7vs &6&l" + secondName).durability(durability).hideMeta()
                    .lore(
                            CC.MENU_BAR,
                            "&fKit: &6" + kitName,
                            "&fArena: &6" + arenaName,
                            "&fFila: &6" + queueName,
                            "&fEspectadores: &6" + match.getSpectators().size(),
                            "&fPágina: &6Clique para entrar em modo espectador",
                            " ",
                            "&aClique para assistir a partida.",
                            CC.MENU_BAR
                    )
                    .hideMeta().build();
        }

        private String resolveParticipantName(int index) {
            if (match.getParticipants() == null || match.getParticipants().size() <= index || match.getParticipants().get(index) == null) {
                return "Desconhecido";
            }

            String conjoinedNames = match.getParticipants().get(index).getConjoinedNames();
            if (conjoinedNames == null || conjoinedNames.trim().isEmpty()) {
                return "Desconhecido";
            }

            return conjoinedNames;
        }

        /**
         * Handles the click event for the button.
         *
         * @param player       the player who clicked the button
         * @param slot         the slot the button was clicked in
         * @param clickType    the type of click
         * @param hotbarButton the hotbar button clicked
         */
        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            if (clickType != ClickType.LEFT) return;

            if (KaosPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId()).getMatch() != null) {
                player.sendMessage(KaosPractice.getInstance().getService(LocaleService.class).getString(GlobalMessagesLocaleImpl.ERROR_YOU_ALREADY_SPECTATING_MATCH));
                return;
            }

            match.addSpectator(player);
            this.playNeutral(player);
        }
    }

    @AllArgsConstructor
    public static class RefreshButton extends Button {
        /**
         * Gets the item stack for the button.
         *
         * @param player the player viewing the button
         * @return the item stack
         */
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.CARPET)
                    .name("&6&lAtualizar")
                    .lore(
                            CC.MENU_BAR,
                            "&7Recarrega a lista de partidas",
                            "&7que estao acontecendo agora.",
                            " ",
                            "&aClique para atualizar.",
                            CC.MENU_BAR
                    )
                    .durability(2)
                    .build();
        }

        /**
         * Handles the click event for the button.
         *
         * @param player       the player who clicked the button
         * @param slot         the slot the button was clicked in
         * @param clickType    the type of click
         * @param hotbarButton the hotbar button clicked
         */
        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            if (clickType != ClickType.LEFT) return;
            new CurrentMatchesMenu().openMenu(player);
            this.playNeutral(player);
        }
    }

    @AllArgsConstructor
    public static class NavigationButton extends Button {
        private final PaginatedMenu menu;
        private final int offset;

        @Override
        public ItemStack getButtonItem(Player player) {
            boolean next = this.offset > 0;

            return new ItemBuilder(Material.ARROW)
                    .name(next ? "&6&lProximo" : "&6&lAnterior")
                    .lore(
                            CC.MENU_BAR,
                            "&fPagina atual: &6" + this.menu.getPage() + "/" + this.menu.getPages(player),
                            " ",
                            next ? "&aClique para ir para a proxima pagina." : "&aClique para voltar uma pagina.",
                            CC.MENU_BAR
                    )
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            if (clickType != ClickType.LEFT) {
                return;
            }

            this.menu.modPage(player, this.offset);
            this.playNeutral(player);
        }
    }

    public static class CloseButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.BARRIER)
                    .name("&c&lFechar")
                    .lore(
                            CC.MENU_BAR,
                            "&7Fecha o menu de partidas atuais.",
                            " ",
                            "&cClique para fechar.",
                            CC.MENU_BAR
                    )
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            if (clickType != ClickType.LEFT) {
                return;
            }

            player.closeInventory();
            this.playNeutral(player);
        }
    }
}
