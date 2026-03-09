package com.kaosmc.practice.feature.kit.command.impl.settings;

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
 * @date 08/10/2024 - 20:04
 */
public class KitViewSettingsCommand extends BaseCommand {
    @CommandData(
            name = "kit.viewsettings",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "kit viewsettings <kitName>",
            description = "Mostra as configurações de um kit."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        Kit kit = this.plugin.getService(KitService.class).getKit(args[0]);
        if (kit == null) {
            sender.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_NOT_FOUND)));
            return;
        }

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&6&lConfigurações do Kit " + kit.getName()));
        kit.getKitSettings().forEach(setting -> sender.sendMessage(CC.translate(" &f◆ &6" + setting.getName() + " &f(" + (setting.isEnabled() ? "&aAtivado" : "&cDesativado") + "&f)")));
        sender.sendMessage("");
    }
}
