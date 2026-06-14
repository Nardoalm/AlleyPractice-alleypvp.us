package us.alleypvp.practice.feature.party.command.impl.leader;

import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.enums.ProfileState;
import us.alleypvp.practice.feature.party.menu.event.PartyEventMenu;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project kaos-practice
 * @date 22/07/2025
 */
public class PartyEventMenuCommand extends BaseCommand {
    @CommandData(
            name = "party.event",
            aliases = {"p.event"},
            usage = "party event",
            description = "Abre o party event menu."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = plugin.getService(ProfileService.class).getProfile(player.getUniqueId());

        if (profile.getParty() == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_NOT_IN_PARTY));
            return;
        }

        if (profile.getState() != ProfileState.LOBBY) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_MUST_BE_IN_LOBBY));
            return;
        }

        new PartyEventMenu().openMenu(player);
    }
}
