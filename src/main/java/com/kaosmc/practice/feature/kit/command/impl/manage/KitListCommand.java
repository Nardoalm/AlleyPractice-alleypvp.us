package com.kaosmc.practice.feature.kit.command.impl.manage;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.feature.kit.KitService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Kaos
 * @date 28/04/2024 - 22:07
 */
public class KitListCommand extends BaseCommand {
    @CommandData(
            name = "kit.list",
            aliases = {"kits"},
            isAdminOnly = true,
            usage = "kit list",
            description = "Envia uma lista de todos os kits."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        KitService kitService = this.plugin.getService(KitService.class);

        sender.sendMessage("");
        sender.sendMessage(CC.translate("     &6&lLista de Kits &f(" + kitService.getKits().size() + "&f)"));
        if (kitService.getKits().isEmpty()) {
            sender.sendMessage(CC.translate("      &f◆ &cNenhum kit disponível."));
        }
        kitService.getKits().forEach(kit -> sender.sendMessage(CC.translate("      &f◆ &6" + kit.getDisplayName() + " &f(" + (kit.isEnabled() ? "&aAtivado" : "&cDesativado") + "&f)")));
        sender.sendMessage("");
    }
}
