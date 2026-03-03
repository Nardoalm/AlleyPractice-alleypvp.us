package com.kaosmc.practice.feature.kit.command.impl.manage;

import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.kit.KitService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
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
