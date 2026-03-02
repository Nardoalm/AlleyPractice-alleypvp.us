package com.kaosmc.practice.feature.kit.command.helper.impl;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @date 28/10/2024 - 09:15
 */
public class RemoveEnchantsCommand extends BaseCommand {
    @CommandData(
            name = "removeenchants",
            aliases = "enchantsremovement",
            isAdminOnly = true,
            usage = "removeenchants",
            description = "Removes all enchantments from the item you're holding."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (player.getInventory().getItemInHand() == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_MUST_HOLD_ITEM));
            return;
        }

        if (player.getInventory().getItemInHand().getEnchantments().isEmpty()) {
            player.sendMessage(CC.translate("&cThe item you're holding doesn't have any enchantments."));
            return;
        }

        player.sendMessage(CC.translate("&cEnchantsments: &f" + player.getInventory().getItemInHand().getEnchantments().keySet()));

        player.getInventory().getItemInHand().getEnchantments().keySet().forEach(enchant -> {
            player.getInventory().getItemInHand().removeEnchantment(enchant);
        });

        player.sendMessage(CC.translate("&aSuccessfully removed all enchantments from the &6" + player.getInventory().getItemInHand().getType().name() + " &aitem."));
    }
}