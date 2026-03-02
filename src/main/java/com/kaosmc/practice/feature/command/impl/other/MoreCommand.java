package com.kaosmc.practice.feature.command.impl.other;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Kaos
 * @date 28/10/2024 - 08:50
 */
public class MoreCommand extends BaseCommand {
    @CommandData(
            name = "more",
            isAdminOnly = true,
            usage = "more <soup/potion> <amount>",
            description = "Gives you more soup or potions."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            command.sendUsage();
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException exception) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_NUMBER).replace("{input}", args[1]));
            return;
        }

        if (args[0].equalsIgnoreCase("soup")) {
            for (int i = 0; i < amount; i++) {
                player.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));
            }
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ITEM_GIVEN)
                    .replace("{item-name}", CC.translate("&6Mushroom Soup"))
                    .replace("{amount}", String.valueOf(amount))
            );
        } else if (args[0].equalsIgnoreCase("potion")) {
            for (int i = 0; i < amount; i++) {
                player.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 16421));
            }
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ITEM_GIVEN)
                    .replace("{item-name}", CC.translate("&6Splash Potion of Healing"))
                    .replace("{amount}", String.valueOf(amount))
            );
        } else {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_ITEM));
        }
    }
}