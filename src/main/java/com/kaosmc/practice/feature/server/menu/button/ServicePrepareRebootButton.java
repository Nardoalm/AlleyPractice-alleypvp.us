package com.kaosmc.practice.feature.server.menu.button;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.feature.server.ServerService;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Kaos
 * @since 09/03/2025
 */
public class ServicePrepareRebootButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.REDSTONE)
                .name("&c&lPrepare Reboot")
                .lore(
                        "&fThis will prepare the server",
                        "&ffor a reboot by cancelling",
                        "&fall tasks and on-going matches.",
                        "",
                        "&cWARNING:",
                        " &fThere won't be a",
                        " &fconfirmation menu.",
                        "",
                        "&cClick to prepare!"
                )
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        if (!player.isOp()) {
            player.sendMessage(CC.translate("&cYou do not have permission to use this button."));
            return;
        }

        ServerService serverService = KaosPractice.getInstance().getService(ServerService.class);

        if (!serverService.isQueueingAllowed()) {
            player.sendMessage(CC.translate("&cThe server is already preparing for a reboot."));
            return;
        }

        serverService.endAllMatches(player);
        serverService.disbandAllParties(player);
        serverService.clearAllQueues(player);

        serverService.setQueueingAllowed(false);
        Arrays.asList(
                "",
                "&c&lServer is preparing for a reboot...",
                ""
        ).forEach(line -> Bukkit.broadcastMessage(CC.translate(line)));
    }
}