package com.kaosmc.practice.feature.party.command.impl.leader;

import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.enums.ProfileState;
import com.kaosmc.practice.feature.party.menu.event.PartyEventMenu;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
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
