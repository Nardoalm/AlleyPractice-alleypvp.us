package com.kaosmc.practice.feature.party.command.impl.leader;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.enums.ProfileState;
import com.kaosmc.practice.feature.party.PartyService;
import com.kaosmc.practice.feature.server.ServerService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Emmy
 * @project Kaos
 * @date 22/05/2024 - 20:33
 */
public class PartyCreateCommand extends BaseCommand {
    @Override
    @CommandData(
            name = "party.create",
            aliases = {"p.create"},
            usage = "party create",
            description = "Create a party."
    )
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        UUID playerUUID = player.getUniqueId();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        PartyService partyService = this.plugin.getService(PartyService.class);
        ServerService serverService = this.plugin.getService(ServerService.class);

        if (profileService.getProfile(playerUUID).getState() != ProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou must be at spawn to execute this command."));
            return;
        }

        if (partyService.getPartyByLeader(player) != null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_ALREADY_IN_PARTY));
            return;
        }

        if (!serverService.isQueueingAllowed()) {
            player.sendMessage(CC.translate("&cYou cannot create a party while server queueing is disabled."));
            return;
        }

        partyService.createParty(player);
    }
}