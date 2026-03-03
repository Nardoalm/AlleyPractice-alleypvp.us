package com.kaosmc.practice.feature.ffa.command.impl.admin.data;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.kit.KitService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @date 21/05/2024 - 00:25
 */
public class FFASetSlotCommand extends BaseCommand {
    @CommandData(
            name = "ffa.setslot",
            isAdminOnly = true,
            usage = "ffa setslot <kitName> <slot>",
            description = "Define o slot de FFA para um kit."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 2) {
            command.sendUsage();
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_NOT_FOUND)));
            return;
        }

        int slot;
        try {
            slot = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_SLOT_MUST_BE_NUMBER)));
            return;
        }

        if (!kit.isFfaEnabled()) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.FFA_DISABLED).replace("{kit-name}", kit.getName()));
            return;
        }

        kit.setFfaSlot(slot);
        kitService.saveKit(kit);
        player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_FFA_SLOT_SET))
                .replace("{kit-name}", kit.getName())
                .replace("{slot}", String.valueOf(slot))
        );
    }
}