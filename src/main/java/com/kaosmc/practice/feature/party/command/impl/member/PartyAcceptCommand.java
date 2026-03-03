package com.kaosmc.practice.feature.party.command.impl.member;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.party.Party;
import com.kaosmc.practice.feature.party.PartyRequest;
import com.kaosmc.practice.feature.party.PartyService;
import com.kaosmc.practice.feature.party.PartyState;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @date 25/05/2024 - 18:33
 */
public class PartyAcceptCommand extends BaseCommand {
    @Override
    @CommandData(
            name = "party.accept",
            aliases = "p.accept",
            usage = "party accept <player>",
            description = "Aceita um convite de party."
    )
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            command.sendUsage();
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(CC.translate("&cO jogador que você está tentando entrar não está online."));
            return;
        }

        PartyService partyService = this.plugin.getService(PartyService.class);

        Party party = partyService.getPartyByLeader(target);
        if (party == null) {
            player.sendMessage(CC.translate("&cO jogador que você está tentando entrar não possui party."));
            return;
        }

        if (party.getState() == PartyState.PUBLIC) {
            partyService.joinParty(player, target);
            return;
        }

        PartyRequest partyRequest = partyService.getRequest(player);
        if (partyRequest == null || !partyRequest.getSender().equals(target)) {
            player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_NO_PARTY_INVITE_FROM_PLAYER)
                    .replace("{name-color}", String.valueOf(this.getProfile(target.getUniqueId()).getNameColor()))
                    .replace("{player}", target.getName()))
            );
            return;
        }

        if (partyRequest.hasExpired()) {
            partyService.removeRequest(partyRequest);
            player.sendMessage(CC.translate("&cO pedido de party expirou."));
            return;
        }

        partyService.joinParty(player, target);
        partyService.removeRequest(partyRequest);
    }
}