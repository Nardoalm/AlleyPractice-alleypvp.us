package us.alleypvp.practice.feature.kit.command.impl.manage;

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
 * @since 11/04/2025
 */
public class KitSetProfileCommand extends BaseCommand {
    @CommandData(
            name = "kit.setprofile",
            aliases = "kit.setkbprofile",
            isAdminOnly = true,
            usage = "kit setprofile <kitName> <profileName>",
            description = "Define o perfil de knockback de um kit."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            command.sendUsage();
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.KIT_NOT_FOUND).replace("{kit-name}", args[0]));
            return;
        }

        kit.setKnockbackProfile(args[1]);
        kitService.saveKit(kit);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.KIT_KB_PROFILE_SET)
                .replace("{kb-profile}", args[1])
                .replace("{kit-name}", args[0]));
    }
}
