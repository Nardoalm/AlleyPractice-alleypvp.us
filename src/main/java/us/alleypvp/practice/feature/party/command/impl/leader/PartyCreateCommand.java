package us.alleypvp.practice.feature.party.command.impl.leader;

import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.enums.ProfileState;
import us.alleypvp.practice.feature.party.PartyService;
import us.alleypvp.practice.feature.server.ServerService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 22/05/2024 - 20:33
 */
public class PartyCreateCommand extends BaseCommand {
    @Override
    @CommandData(
            name = "party.create",
            aliases = {"p.create"},
            usage = "party create",
            description = "Cria uma party."
    )
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        UUID playerUUID = player.getUniqueId();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        PartyService partyService = this.plugin.getService(PartyService.class);
        ServerService serverService = this.plugin.getService(ServerService.class);

        if (profileService.getProfile(playerUUID).getState() != ProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou must be in the spawn to execute this command."));
            return;
        }

        if (partyService.getPartyByLeader(player) != null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_ALREADY_IN_PARTY));
            return;
        }

        if (!serverService.isQueueingAllowed()) {
            player.sendMessage(CC.translate("&cYou cannot create a party while the server queues are disabled."));
            return;
        }

        partyService.createParty(player);
    }
}