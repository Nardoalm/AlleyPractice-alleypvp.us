package com.kaosmc.practice.feature.duel.menu;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.feature.duel.DuelRequest;
import com.kaosmc.practice.feature.duel.DuelRequestService;
import com.kaosmc.practice.feature.server.ServerService;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.library.menu.pagination.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Kaos
 * @date 22/10/2024 - 18:18
 */
public class DuelRequestsMenu extends PaginatedMenu {
    protected final KaosPractice plugin = KaosPractice.getInstance();

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&6&lPedidos de Duelo";
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        this.addGlassHeader(buttons, 15);

        buttons.put(4, new RefreshDuelRequestsButton());

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        KaosPractice.getInstance().getService(DuelRequestService.class).getDuelRequests()
                .stream()
                .filter(duelRequest -> !duelRequest.getSender().equals(player))
                .forEach(duelRequest -> buttons.put(buttons.size(), new DuelRequestsButton(duelRequest)));


        return buttons;
    }

    @AllArgsConstructor
    private static class DuelRequestsButton extends Button {
        protected final KaosPractice plugin = KaosPractice.getInstance();
        private DuelRequest duelRequest;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.PAPER).name("&6&l" + this.duelRequest.getSender().getName()).durability(0).hideMeta()
                    .lore(
                            "&fKit: &f" + this.duelRequest.getKit().getDisplayName(),
                            "&fArena: &f" + this.duelRequest.getArena().getDisplayName(),
                            "",
                            "&fExpira em: &6" + this.duelRequest.getRemainingTimeFormatted(),
                            "",
                            "&aClique para aceitar!"
                    )
                    .hideMeta().build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            LocaleService localeService = KaosPractice.getInstance().getService(LocaleService.class);

            if (this.duelRequest.hasExpired()) {
                player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.ERROR_DUEL_REQUESTS_EXPIRED));
                new DuelRequestsMenu().openMenu(player);
                return;
            }

            if (this.duelRequest.getArena() == null) {
                player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.ERROR_DUEL_REQUESTS_NO_ARENA));
                new DuelRequestsMenu().openMenu(player);
                return;
            }

            if (KaosPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId()).getMatch() != null) {
                player.sendMessage(this.plugin.getService(LocaleService.class).getString(GlobalMessagesLocaleImpl.ERROR_YOU_ALREADY_PLAYING_MATCH));
                return;
            }

            ServerService serverService = KaosPractice.getInstance().getService(ServerService.class);
            if (!serverService.isQueueingAllowed()) {
                player.sendMessage(KaosPractice.getInstance().getService(LocaleService.class).getString(GlobalMessagesLocaleImpl.QUEUE_TEMPORARILY_DISABLED));
                player.closeInventory();
                return;
            }

            KaosPractice.getInstance().getService(DuelRequestService.class).acceptPendingRequest(this.duelRequest);
            player.closeInventory();
        }
    }

    @AllArgsConstructor
    private static class RefreshDuelRequestsButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.EMERALD)
                    .name("&a&lAtualizar")
                    .lore("&aClique para atualizar os pedidos de duelo.")
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            new DuelRequestsMenu().openMenu(player);
        }
    }
}
