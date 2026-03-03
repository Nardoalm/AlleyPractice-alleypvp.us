package com.kaosmc.practice.feature.hotbar.command.impl;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.hotbar.HotbarItem;
import com.kaosmc.practice.feature.hotbar.HotbarService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * @author Emmy
 * @project kaos-practice
 * @since 26/07/2025
 */
public class HotbarListCommand extends BaseCommand {
    @CommandData(
            name = "hotbar.list",
            isAdminOnly = true,
            usage = "hotbar list",
            description = "Envia uma lista de todos os itens da hotbar."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        HotbarService hotbarService = this.plugin.getService(HotbarService.class);

        Collection<HotbarItem> hotbarItems = hotbarService.getHotbarItems();

        if (hotbarItems.isEmpty()) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.HOTBAR_NO_HOTBAR_ITEMS_CREATED));
            return;
        }

        player.sendMessage(CC.translate("&6Itens da Hotbar:"));
        for (HotbarItem item : hotbarItems) {
            player.sendMessage(CC.translate(" &e• &f" + item.getName()));
        }
    }
}