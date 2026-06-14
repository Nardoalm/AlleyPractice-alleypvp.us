package us.alleypvp.practice.feature.kit.command.impl.data;

import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.feature.kit.Kit;
import us.alleypvp.practice.feature.kit.KitService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @date 08/10/2024 - 19:41
 */
public class KitSetDisclaimerCommand extends BaseCommand {
    @CommandData(
            name = "kit.setdisclaimer",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "kit setdisclaimer <kitName> <disclaimer>",
            description = "Define o aviso de um kit."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            command.sendUsage();
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            sender.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_NOT_FOUND)));
            return;
        }

        String disclaimer = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        kit.setDisclaimer(disclaimer);
        kitService.saveKit(kit);
        sender.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_DISCLAIMER_SET)).replace("{kit-name}", kit.getName()).replace("{disclaimer}", disclaimer));
    }
}