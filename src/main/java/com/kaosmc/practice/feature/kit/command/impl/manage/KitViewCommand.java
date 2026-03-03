package com.kaosmc.practice.feature.kit.command.impl.manage;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.common.text.ClickableUtil;
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
 * @date 08/10/2024 - 19:56
 */
public class KitViewCommand extends BaseCommand {
    @CommandData(
            name = "kit.view",
            aliases = "kit.info",
            isAdminOnly = true,
            usage = "kit view <kitName>",
            description = "Ver information about a kit."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        Kit kit = this.plugin.getService(KitService.class).getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_NOT_FOUND)));
            return;
        }

        player.sendMessage("");
        player.sendMessage(CC.translate("&6&lKit " + kit.getName() + " &f(" + (kit.isEnabled() ? "&aEnabled" : "&cDisabled") + "&f)"));
        player.sendMessage(CC.translate(" &f◆ &6Display Name: &f" + kit.getDisplayName()));
        player.sendMessage(CC.translate(" &f◆ &6Name: &f" + kit.getName()));
        player.sendMessage(CC.translate(" &f◆ &6Icon: &f" + kit.getIcon().name().toLowerCase() + " &7(" + kit.getDurability() + ")"));
        player.sendMessage(CC.translate(" &f◆ &6Disclaimer: &f" + kit.getDisclaimer()));
        player.sendMessage(CC.translate(" &f◆ &6Description: &f" + kit.getDescription()));
        player.sendMessage(CC.translate(" &f◆ &6FFA: &f" + (kit.isFfaEnabled() ? "&aEnabled" : "&cDisabled")));
        player.spigot().sendMessage(ClickableUtil.createComponent(
                "  &a(Click here to view the kit settings)",
                "/kit viewsettings " + kit.getName(),
                "&7Click to view the settings of the kit &6" + kit.getName())
        );
        player.sendMessage("");
    }
}