package com.kaosmc.practice.feature.command.impl.other;

import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import com.kaosmc.practice.common.text.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Kaos
 * @date 28/10/2024 - 08:47
 */
public class RefillCommand extends BaseCommand {
    @CommandData(
            name = "refill",
            isAdminOnly = true,
            usage = "refill",
            description = "Reabastece seu inventário com poções de vida."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Arrays.stream(player.getInventory().getContents()).forEach(item -> {
            if (item == null) {
                player.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 16421));
            }
        });

        player.sendMessage(CC.translate("&aVocê reabasteceu seu inventário com &6poções de vida&a."));
    }
}