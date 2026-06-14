package us.alleypvp.practice.feature.match.command.player;

import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.enums.ProfileState;
import us.alleypvp.practice.feature.match.menu.SpectatorTeleportMenu;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 26/06/2025
 */
public class ViewPlayersCommand extends BaseCommand {
    @CommandData(
            name = "viewplayers",
            usage = "viewplayers",
            description = "Mostra todos os jogadores da partida (somente espectador)."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.SPECTATING) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_NOT_SPECTATING_MATCH));
            return;
        }

        new SpectatorTeleportMenu(profile.getMatch()).openMenu(player);
    }
}
