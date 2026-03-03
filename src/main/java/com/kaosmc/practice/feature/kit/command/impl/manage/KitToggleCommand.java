package com.kaosmc.practice.feature.kit.command.impl.manage;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.kit.KitService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Kaos
 * @since 11/04/2025
 */
public class KitToggleCommand extends BaseCommand {
    @CommandData(
            name = "kit.toggle",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "kit toggle <kitName>",
            description = "Alterna o status habilitado de um kit."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        String kitName = args[0];
        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(kitName);
        if (kit == null) {
            sender.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_NOT_FOUND)));
            return;
        }

        if (kit.getIcon() == null) {
            sender.sendMessage(CC.translate("&cEste kit não possui ícone definido. Defina um ícone antes de alternar."));
            return;
        }

        if (kit.getCategory() == null) {
            sender.sendMessage(CC.translate("&cEste kit não possui categoria definida. Defina uma categoria antes de alternar."));
            return;
        }

        if (kit.getKitSettings().isEmpty()) {
            sender.sendMessage(CC.translate("&cEste kit não possui configurações definidas. Configure as opções do kit antes de alternar."));
            return;
        }

        kit.setEnabled(!kit.isEnabled());
        kitService.saveKit(kit);
        String status = kit.isEnabled() ? CC.translate("&aenabled") : CC.translate("&cdisabled");
        sender.sendMessage(CC.translate("&aSuccessfully " + status + " &athe kit &6" + kit.getName() + "&a."));
    }
}