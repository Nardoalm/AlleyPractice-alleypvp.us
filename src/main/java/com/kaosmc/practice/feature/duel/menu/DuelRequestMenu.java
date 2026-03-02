package com.kaosmc.practice.feature.duel.menu;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.SettingsLocaleImpl;
import com.kaosmc.practice.feature.duel.DuelRequestService;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.queue.Queue;
import com.kaosmc.practice.feature.queue.QueueService;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.library.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Kaos
 * @date 17/10/2024 - 20:11
 */
@AllArgsConstructor
public class DuelRequestMenu extends Menu {
    protected final KaosPractice plugin = KaosPractice.getInstance();
    private final Player targetPlayer;

    @Override
    public String getTitle(Player player) {
        return "&6&lDuel " + this.targetPlayer.getName();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int slot = 10;
        for (Queue queue : KaosPractice.getInstance().getService(QueueService.class).getQueues()) {
            if (!queue.isRanked() && !queue.isDuos() && queue.getKit().isEnabled()) {
                slot = this.skipIfSlotCrossingBorder(slot);
                buttons.put(slot++, new DuelButton(this.targetPlayer, queue.getKit()));
            }
        }

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 6;
    }

    @AllArgsConstructor
    private static class DuelButton extends Button {
        protected final KaosPractice plugin = KaosPractice.getInstance();
        private Player targetPlayer;
        private Kit kit;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(this.kit.getIcon())
                    .name(this.kit.getMenuTitle())
                    .lore(
                            "",
                            "&aClick to select!"
                    )
                    .durability(this.kit.getDurability())
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            if (player.hasPermission(this.plugin.getService(LocaleService.class).getString(SettingsLocaleImpl.PERMISSION_DONATOR_DUEL_ARENA_SELECTOR))) {
                new DuelArenaSelectorMenu(this.targetPlayer, this.kit).openMenu(player);
                return;
            }

            player.closeInventory();

            KaosPractice.getInstance().getService(DuelRequestService.class).createAndSendRequest(player, this.targetPlayer, this.kit, null);
        }
    }
}