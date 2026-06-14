package us.alleypvp.practice.feature.kit.command.impl.settings;

import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.feature.kit.Kit;
import us.alleypvp.practice.feature.kit.KitService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
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
        sender.sendMessage(CC.translate("&b&lConfigurações do Kit " + kit.getName()));
        kit.getKitSettings().forEach(setting -> sender.sendMessage(CC.translate(" &f◆ &b" + setting.getName() + " &f(" + (setting.isEnabled() ? "&aAtivado" : "&cDesativado") + "&f)")));
        sender.sendMessage("");
    }
}
