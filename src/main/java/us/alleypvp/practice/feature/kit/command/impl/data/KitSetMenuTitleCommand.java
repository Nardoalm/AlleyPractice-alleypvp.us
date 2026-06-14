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
 * @since 25/06/2025
 */
public class KitSetMenuTitleCommand extends BaseCommand {
    @CommandData(
            name = "kit.setmenutitle",
            aliases = "kit.menutitle",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "kit setmenutitle <kitName> <title>",
            description = "Define o título do menu de um kit."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
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

        String title = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        kit.setMenuTitle(title);
        kitService.saveKit(kit);
        sender.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_MENU_TITLE_SET).replace("{kit-name}", kit.getName()).replace("{title}", title)));
    }
}