package com.kaosmc.practice.feature.duel.menu;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.feature.arena.Arena;
import com.kaosmc.practice.feature.arena.ArenaService;
import com.kaosmc.practice.feature.duel.DuelRequestService;
import com.kaosmc.practice.feature.kit.Kit;
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
 * @date 17/10/2024 - 22:13
 */
@AllArgsConstructor
public class DuelArenaSelectorMenu extends PaginatedMenu {
    private Player targetPlayer;
    private Kit kit;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&6&lSelecione uma arena";
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        this.addGlassHeader(buttons, 15);

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int slot = 0;

        for (Arena arena : KaosPractice.getInstance().getService(ArenaService.class).getArenas()) {
            if (arena.getKits().contains(this.kit.getName()) && arena.isEnabled()) {
                buttons.put(slot++, new DuelArenaSelectorButton(this.targetPlayer, this.kit, arena));
            }
        }

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 6;
    }

    @AllArgsConstructor
    private static class DuelArenaSelectorButton extends Button {
        private Player targetPlayer;
        private Kit kit;
        private Arena arena;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.PAPER)
                    .name("&6&l" + this.arena.getDisplayName())
                    .lore(
                            " &f◆ &6Alvo: &f" + this.targetPlayer.getName(),
                            " &f◆ &6Kit: &f" + this.kit.getDisplayName(),
                            "",
                            "&aClique para enviar o pedido!"
                    )
                    .durability(0)
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            player.closeInventory();

            KaosPractice.getInstance().getService(DuelRequestService.class).createAndSendRequest(player, this.targetPlayer, this.kit, this.arena);
        }
    }
}
