package us.alleypvp.practice.feature.kit.command.impl.storage;

import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.feature.kit.Kit;
import us.alleypvp.practice.feature.kit.KitService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 01:11
 */
public class KitSaveCommand extends BaseCommand {
    @CommandData(
            name = "kit.save",
            isAdminOnly = true,
            usage = "kit save <kitName>",
            description = "Salva um kit no banco de dados."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            command.sendUsage();
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_NOT_FOUND)));
            return;
        }

        kitService.saveKit(kit);
        player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_SAVED).replace("{kit-name}", kit.getDisplayName())));
    }
}