package com.kaosmc.practice.feature.kit.command.impl.data.potion;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.kit.KitService;
import com.kaosmc.practice.feature.kit.menu.KitPotionListMenu;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @since 16/06/2025
 */
public class KitRemovePotionCommand extends BaseCommand {
    @CommandData(
            name = "kit.removepotion",
            isAdminOnly = true,
            usage = "kit removepotion <kitName>",
            description = "Remover um efeito de poção de um kit."
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
        Kit kit = kitService.getKit(kitName);
        if (kit == null) {
            player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_NOT_FOUND)).replace("{kit}", kitName));
            return;
        }

        new KitPotionListMenu(kit).openMenu(player);
    }
}