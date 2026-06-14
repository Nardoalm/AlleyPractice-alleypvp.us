package us.alleypvp.practice.feature.hotbar.command.impl;

import us.alleypvp.practice.common.text.EnumFormatter;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.feature.hotbar.HotbarService;
import us.alleypvp.practice.feature.hotbar.HotbarType;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project kaos-practice
 * @since 21/07/2025
 */
public class HotbarCreateCommand extends BaseCommand {
    @CommandData(
            name = "hotbar.create",
            isAdminOnly = true,
            usage = "hotbar create <name> <type>",
            description = "Cria um novo item de hotbar."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            command.sendUsage();
            return;
        }

        String name = args[0];

        HotbarType type;
        try {
            type = HotbarType.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException exception) {
            player.sendMessage(EnumFormatter.outputAvailableValues(HotbarType.class));
            return;
        }

        this.plugin.getService(HotbarService.class).createHotbarItem(name, type);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.HOTBAR_CREATED_ITEM)
                .replace("{name}", name)
                .replace("{type}", type.name())
        );
    }
}