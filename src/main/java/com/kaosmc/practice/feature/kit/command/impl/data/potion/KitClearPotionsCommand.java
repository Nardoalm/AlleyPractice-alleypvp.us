package com.kaosmc.practice.feature.kit.command.impl.data.potion;

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
 * @since 16/06/2025
 */
public class KitClearPotionsCommand extends BaseCommand {
    @CommandData(
            name = "kit.clearpotions",
            isAdminOnly = true,
            usage = "kit clearpotions <kitName>",
            description = "Clear all potion effects from a kit."
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

        if (kit.getPotionEffects().isEmpty()) {
            player.sendMessage(CC.translate("&cThis kit has no potion effects to remove."));
            return;
        }

        kit.getPotionEffects().clear();
        kitService.saveKit(kit);
        player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_POTION_EFFECTS_CLEARED).replace("{kit-name}", kit.getName())));
    }
}
