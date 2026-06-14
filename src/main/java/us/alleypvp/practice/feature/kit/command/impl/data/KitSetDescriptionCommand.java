package us.alleypvp.practice.feature.kit.command.impl.data;

import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.feature.kit.Kit;
import us.alleypvp.practice.feature.kit.KitService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @date 28/04/2024 - 22:46
 */
public class KitSetDescriptionCommand extends BaseCommand {
    @CommandData(
            name = "kit.description",
            aliases = "kit.setdesc",
            isAdminOnly = true,
            usage = "kit description <kitName> <description/clear>",
            description = "Define ou limpa a descrição de um kit."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player sender = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 2) {
            command.sendUsage();
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            sender.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_NOT_FOUND)));
            return;
        }

        if (args[1].equalsIgnoreCase("clear")) {
            kit.setDescription("");
            this.plugin.getService(KitService.class).saveKit(kit);
            sender.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_DESCRIPTION_CLEARED).replace("{kit-name}", kit.getName())));
            return;
        }

        String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        kit.setDescription(description);
        this.plugin.getService(KitService.class).saveKit(kit);
        sender.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_DESCRIPTION_SET).replace("{kit-name}", kit.getName()).replace("{description}", description)));
    }
}