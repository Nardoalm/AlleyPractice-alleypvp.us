package us.alleypvp.practice.feature.layout.command;

import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.enums.ProfileState;
import us.alleypvp.practice.feature.layout.LayoutService;
import us.alleypvp.practice.library.menu.Menu;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
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
