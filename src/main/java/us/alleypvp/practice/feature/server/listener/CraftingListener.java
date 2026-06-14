package us.alleypvp.practice.feature.server.listener;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.feature.server.ServerService;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.enums.ProfileState;
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

        Profile profile = AlleyPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.PLAYING && profile.getState() != ProfileState.FFA && profile.getState() != ProfileState.SPECTATING) {
            return;
        }

        ItemStack result = event.getInventory().getResult();
        if (result == null || result.getType() == Material.AIR) {
            return;
        }

        if (AlleyPractice.getInstance().getService(ServerService.class).getBlockedCraftingItems().contains(result.getType())) {
            event.getInventory().setResult(null);
        }
    }
}