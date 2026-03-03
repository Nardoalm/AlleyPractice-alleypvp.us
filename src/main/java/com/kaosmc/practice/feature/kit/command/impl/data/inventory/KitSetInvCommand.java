package com.kaosmc.practice.feature.kit.command.impl.data.inventory;

import com.kaosmc.practice.common.InventoryUtil;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.kit.KitService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Kaos
 * @date 28/04/2024 - 22:23
 */
public class KitSetInvCommand extends BaseCommand {
    @CommandData(
            name = "kit.setinventory",
            aliases = "kit.setinv",
            isAdminOnly = true,
            usage = "kit setinventory <kitName>",
            description = "Define o inventário e a armadura de um kit para seu inventário e armadura atuais."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_NOT_FOUND)));
            return;
        }

        ItemStack[] inventory = InventoryUtil.cloneItemStackArray(player.getInventory().getContents());
        ItemStack[] armor = InventoryUtil.cloneItemStackArray(player.getInventory().getArmorContents());

        kit.setItems(inventory);
        kit.setArmor(armor);
        kitService.saveKit(kit);

        //TODO: reset saved layouts for this kit for all players

        player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_INVENTORY_SET).replace("{kit-name}", kit.getName())));
    }
}