package us.alleypvp.practice.feature.hotbar.listener;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.library.menu.Menu;
import us.alleypvp.practice.feature.hotbar.HotbarItem;
import us.alleypvp.practice.feature.hotbar.HotbarService;
import us.alleypvp.practice.feature.hotbar.HotbarAction;
import us.alleypvp.practice.feature.hotbar.HotbarType;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.common.logger.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class HotbarListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if ((action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);
        HotbarService hotbarService = AlleyPractice.getInstance().getService(HotbarService.class);

        Player player = event.getPlayer();
        ItemStack clickedItem = player.getItemInHand();

        if (clickedItem == null || !clickedItem.hasItemMeta() || !clickedItem.getItemMeta().hasDisplayName()) {
            return;
        }

        Profile profile = profileService.getProfile(player.getUniqueId());
        HotbarType currentHotbarType = hotbarService.getCorrespondingType(profile);
        if (currentHotbarType == null) return;

        HotbarItem hotbarItem = hotbarService.getHotbarItem(clickedItem, currentHotbarType);
        if (hotbarItem == null) {
            return;
        }

        HotbarAction actionType = hotbarItem.getActionData().getAction();
        if (actionType == null) {
            Logger.error("Hotbar item action type is null for item: " + hotbarItem.getName());
            return;
        }

        switch (actionType) {
            case RUN_COMMAND:
                player.performCommand(hotbarItem.getActionData().getCommand());
                break;
            case OPEN_MENU:
                Menu menu = hotbarService.getMenuInstanceFromName(hotbarItem.getActionData().getMenuName(), player);
                menu.openMenu(player);
                break;
        }

        event.setCancelled(true);
    }
}