package com.kaosmc.practice.feature.kit.command.impl.storage;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.kit.KitService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @date 23/05/2024 - 01:18
 */
public class KitSaveAllCommand extends BaseCommand {
    @CommandData(
            name = "kit.saveall",
            isAdminOnly = true,
            usage = "kit saveall",
            description = "Save all kits to storage."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        this.plugin.getService(KitService.class).saveKits();
        player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_SAVED_ALL)));
    }
}