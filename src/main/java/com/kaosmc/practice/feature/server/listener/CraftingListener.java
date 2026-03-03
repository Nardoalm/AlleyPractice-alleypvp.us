package com.kaosmc.practice.feature.server.listener;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.feature.server.ServerService;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.enums.ProfileState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project kaos-practice
 * @since 15/07/2025
 */
public class CraftingListener implements Listener {
    @EventHandler
    private void onPrepareItemCraft(PrepareItemCraftEvent event) {
        Player player = (Player) event.getView().getPlayer();
        if (player == null) {
            return;
        }

        Profile profile = KaosPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.PLAYING && profile.getState() != ProfileState.FFA && profile.getState() != ProfileState.SPECTATING) {
            return;
        }

        ItemStack result = event.getInventory().getResult();
        if (result == null || result.getType() == Material.AIR) {
            return;
        }

        if (KaosPractice.getInstance().getService(ServerService.class).getBlockedCraftingItems().contains(result.getType())) {
            event.getInventory().setResult(null);
        }
    }
}