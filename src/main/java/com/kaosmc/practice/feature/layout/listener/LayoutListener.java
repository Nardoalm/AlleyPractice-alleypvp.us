package com.kaosmc.practice.feature.layout.listener;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.feature.layout.data.LayoutData;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.common.text.CC;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Emmy
 * @project Kaos
 * @since 04/05/2025
 */
public class LayoutListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.BOOK) return;
        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;

        String clickedName = ChatColor.stripColor(item.getItemMeta().getDisplayName());

        for (List<LayoutData> layoutList : KaosPractice.getInstance().getService(ProfileService.class)
                .getProfile(player.getUniqueId())
                .getProfileData().getLayoutData().getLayouts().values()) {

            for (LayoutData layout : layoutList) {
                if (ChatColor.stripColor(layout.getDisplayName()).equalsIgnoreCase(clickedName)) {
                    player.getInventory().setContents(layout.getItems());
                    player.sendMessage(CC.translate("&aYou have selected the layout &6" + layout.getDisplayName() + "&a."));
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}