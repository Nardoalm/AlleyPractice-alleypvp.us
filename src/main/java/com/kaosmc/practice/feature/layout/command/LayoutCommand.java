package com.kaosmc.practice.feature.layout.command;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.enums.ProfileState;
import com.kaosmc.practice.feature.layout.LayoutService;
import com.kaosmc.practice.library.menu.Menu;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @since 03/05/2025
 */
public class LayoutCommand extends BaseCommand {
    @CommandData(
            name = "layout",
            aliases = {"layouteditor", "kiteditor"},
            usage = "layout",
            description = "Edit the layout of a kit."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        if (profileService == null) {
            player.sendMessage(CC.translate("&cNao foi possivel abrir o editor agora."));
            return;
        }
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile == null) {
            player.sendMessage(CC.translate("&cNao foi possivel abrir o editor agora."));
            return;
        }

        if (profile.getState() == ProfileState.WAITING) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            player.sendMessage(CC.translate("&cSaia da fila para abrir o editor de layout."));
            return;
        }

        if (profile.getState() != ProfileState.LOBBY) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_MUST_BE_IN_LOBBY));
            return;
        }

        LayoutService layoutService = this.plugin.getService(LayoutService.class);
        Menu layoutMenu = layoutService != null ? layoutService.getLayoutMenu() : null;
        if (layoutMenu == null) {
            player.sendMessage(CC.translate("&cNao foi possivel abrir o editor agora."));
            return;
        }

        layoutMenu.openMenu(player);
    }
}
