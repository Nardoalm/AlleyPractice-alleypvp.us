package com.kaosmc.practice.feature.kit.command.impl.manage;

import com.kaosmc.practice.common.InventoryUtil;
import com.kaosmc.practice.common.reflect.ReflectionService;
import com.kaosmc.practice.common.reflect.internal.types.ActionBarReflectionServiceImpl;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.kit.KitService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Kaos
 * @date 20/05/2024 - 13:06
 */
public class KitCreateCommand extends BaseCommand {
    @CommandData(
            name = "kit.create",
            isAdminOnly = true,
            usage = "kit create <kitName>",
            description = "Create a kit with your current inventory and armor."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        String kitName = args[0];
        KitService kitService = this.plugin.getService(KitService.class);
        if (kitService.getKit(kitName) != null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.KIT_ALREADY_EXISTS));
            return;
        }

        ItemStack[] inventory = InventoryUtil.cloneItemStackArray(player.getInventory().getContents());
        ItemStack[] armor = InventoryUtil.cloneItemStackArray(player.getInventory().getArmorContents());

        Material icon = Material.DIAMOND_SWORD;
        if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
            icon = player.getItemInHand().getType();
        }

        kitService.createKit(kitName, inventory, armor, icon);

        String message = this.getString(GlobalMessagesLocaleImpl.KIT_CREATED).replace("{kit-name}", kitName);

        player.sendMessage(message);
        this.plugin.getService(ReflectionService.class).getReflectionService(ActionBarReflectionServiceImpl.class).sendMessage(player, message, 5);
        player.sendMessage("");
        player.sendMessage(CC.translate("&7Do not forget to reload the queues &c&lAFTER ENABLING &7 the kit &8(/kit toggle) &7by using &c&l/queue reload&7."));
        player.sendMessage("");
    }
}