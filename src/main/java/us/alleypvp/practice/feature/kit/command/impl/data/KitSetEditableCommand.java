package us.alleypvp.practice.feature.kit.command.impl.data;

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
 * @since 04/05/2025
 */
public class KitSetEditableCommand extends BaseCommand {
    @CommandData(
            name = "kit.seteditable",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "kit seteditable <name> <true/false>",
            description = "Define se um kit é editável ou não."
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

        boolean editable;
        try {
            editable = Boolean.parseBoolean(args[1]);
        } catch (Exception exception) {
            sender.sendMessage(CC.translate("&cValor inválido para editable! Use true ou false."));
            return;
        }

        kit.setEditable(editable);
        kitService.saveKit(kit);
        sender.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_SET_EDITABLE)
                .replace("{kit-name}", kit.getName())
                .replace("{editable}", String.valueOf(editable))));
    }
}
