package com.kaosmc.practice.feature.arena.listener;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.arena.selection.ArenaSelection;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Kaos
 * @date 5/20/2024
 */
public class ArenaListener implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    private void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack itemStack = event.getItem();
            if (itemStack != null && itemStack.equals(ArenaSelection.SELECTION_TOOL)) {
                Player player = event.getPlayer();

                Block clickedBlock = event.getClickedBlock();
                int locationType = 0;

                ArenaSelection arenaSelection = ArenaSelection.createSelection(player);

                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    arenaSelection.setMaximum(clickedBlock.getLocation());
                    locationType = 2;
                } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    arenaSelection.setMinimum(clickedBlock.getLocation());
                    locationType = 1;
                }

                event.setCancelled(true);
                event.setUseItemInHand(PlayerInteractEvent.Result.DENY);
                event.setUseInteractedBlock(PlayerInteractEvent.Result.DENY);

                int getBlockX = clickedBlock.getLocation().getBlockX();
                int getBlockY = clickedBlock.getLocation().getBlockY();
                int getBlockZ = clickedBlock.getLocation().getBlockZ();

                player.sendMessage(CC.translate(KaosPractice.getInstance().getService(LocaleService.class).getString(GlobalMessagesLocaleImpl.ARENA_SELECTED_BOUNDARY)
                        .replace("{boundary-type}", locationType == 1 ? "minimum" : "maximum")
                        .replace("{x}", String.valueOf(getBlockX))
                        .replace("{y}", String.valueOf(getBlockY))
                        .replace("{z}", String.valueOf(getBlockZ))
                ));
            }
        }
    }
}