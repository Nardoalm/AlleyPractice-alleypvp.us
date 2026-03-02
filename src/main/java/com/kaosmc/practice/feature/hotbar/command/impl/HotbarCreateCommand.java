package com.kaosmc.practice.feature.hotbar.command.impl;

import com.kaosmc.practice.common.text.EnumFormatter;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.hotbar.HotbarService;
import com.kaosmc.practice.feature.hotbar.HotbarType;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 21/07/2025
 */
public class HotbarCreateCommand extends BaseCommand {
    @CommandData(
            name = "hotbar.create",
            isAdminOnly = true,
            usage = "hotbar create <name> <type>",
            description = "Create a new hotbar item."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            command.sendUsage();
            return;
        }

        String name = args[0];

        HotbarType type;
        try {
            type = HotbarType.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException exception) {
            player.sendMessage(EnumFormatter.outputAvailableValues(HotbarType.class));
            return;
        }

        this.plugin.getService(HotbarService.class).createHotbarItem(name, type);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.HOTBAR_CREATED_ITEM)
                .replace("{name}", name)
                .replace("{type}", type.name())
        );
    }
}