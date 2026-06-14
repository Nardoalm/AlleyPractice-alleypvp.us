package us.alleypvp.practice.feature.party.command.impl.member;

import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.feature.party.Party;
import us.alleypvp.practice.feature.party.PartyService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 18:27
 */
public class PartyInviteCommand extends BaseCommand {
    @Override
    @CommandData(
            name = "party.invite",
            aliases = "p.invite",
            usage = "party invite <player>",
            description = "Convida um jogador para sua party."
    )
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        PartyService partyService = this.plugin.getService(PartyService.class);
        ProfileService profileService = this.plugin.getService(ProfileService.class);

        if (command.length() < 1) {
            command.sendUsage();
            return;
        }

        String target = args[0];
        Player targetPlayer = Bukkit.getPlayer(target);

        if (targetPlayer == null) {
            player.sendMessage(CC.translate("&cO jogador que você está tentando convidar não está online."));
            return;
        }

        if (targetPlayer == command.getPlayer()) {
            player.sendMessage(CC.translate("&cVocê não pode convidar a si mesmo para uma party."));
            return;
        }

        Party party = partyService.getPartyByMember(player.getUniqueId());
        if (party == null) {
            player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_NOT_IN_PARTY)));
            return;
        }

        if (party.getLeader() != player) {
            player.sendMessage(CC.translate("&cVocê precisa ser o líder da party para convidar jogadores."));
            return;
        }

        Profile targetProfile = profileService.getProfile(targetPlayer.getUniqueId());
        if (!targetProfile.getProfileData().getSettingData().isPartyInvitesEnabled()) {
            player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.ERROR_PLAYER_PARTY_INVITES_DISABLED)
                    .replace("{name-color}", String.valueOf(targetProfile.getNameColor()))
                    .replace("{player}", target))
            );
            return;
        }

        if (party.getMembers().contains(targetPlayer.getUniqueId())) {
            player.sendMessage(CC.translate("&b" + targetPlayer.getName() + " &cis already in your party."));
            return;
        }

        if (partyService.sendInvite(party, player, targetPlayer)) {
            party.notifyParty("&b" + targetPlayer.getName() + " &awas invited to the party by &b" + player.getName() + "&a.");
        }
    }
}
