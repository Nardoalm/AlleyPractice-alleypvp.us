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
public class ServiceResetRebootButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.EMERALD)
                .name("&a&lUnprepare Reboot")
                .lore(
                        "&fThis will undo the preparation",
                        "&ffor a reboot by allowing",
                        "&fqueueing again.",
                        "",
                        "&aClick to allow queueing again!"
                )
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        if (!player.isOp()) {
            player.sendMessage(CC.translate("&cVocê não tem permissão para usar este botão."));
            return;
        }

        KaosPractice.getInstance().getService(ServerService.class).setQueueingAllowed(true);
        Arrays.asList(
                "",
                "&a&lQUEUEING IS NO LONGER DISABLED!",
                ""
        ).forEach(line -> Bukkit.broadcastMessage(CC.translate(line)));
    }
}
