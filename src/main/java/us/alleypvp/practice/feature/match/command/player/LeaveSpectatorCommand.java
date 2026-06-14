package us.alleypvp.practice.feature.match.command.player;

import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.enums.ProfileState;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
public class LeaveSpectatorCommand extends BaseCommand {
    @CommandData(
            name = "leavespectator",
            aliases = {"unspec"},
            usage = "leavespectator",
            description = "Para de assistir uma partida."
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

        if (profile.getFfaMatch() != null && profile.getMatch() == null) {
            profile.getFfaMatch().removeSpectator(player);
            return;
        }

        profile.getMatch().removeSpectator(player, true);
    }
}
