package com.kaosmc.practice.feature.kit.command.helper.impl;

import com.kaosmc.practice.common.reflect.utility.ReflectionUtility;
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
 * @date 14/07/2025
 */
public class GlowCommand extends BaseCommand {
    @CommandData(
            name = "glow",
            description = "Alterna o brilho de encantamento no item da sua mão",
            usage = "glow <true|false>",
            isAdminOnly = true
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_MUST_HOLD_ITEM));
            return;
        }

        boolean glow = Boolean.parseBoolean(args[0]);

        ItemStack result = ReflectionUtility.setGlowing(item, glow);
        player.setItemInHand(result);

        player.sendMessage(CC.translate("&aGlow has been &6" + (glow ? "enabled" : "disabled") + "&a for your item."));
    }
}
