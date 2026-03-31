package com.kaosmc.practice.feature.party.command;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.feature.party.Party;
import com.kaosmc.practice.feature.party.PartyService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @date 22/05/2024 - 20:32
 */
public class PartyCommand extends BaseCommand {
    @CommandData(
            name = "party",
            aliases = "p",
            usage = "party",
            description = "Envia uma lista de comandos de party."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 1) {
            this.handleDirectInvite(player, args[0]);
            return;
        }

        player.sendMessage("");
        player.sendMessage(CC.translate("§eUso do §6/party§e:"));
        player.sendMessage(CC.translate(" &f• &6/party create &7| Criar uma party"));
        player.sendMessage(CC.translate(" &f• &6/party disband &7| Desfazer uma party"));
        player.sendMessage(CC.translate(" &f• &6/party leave &7| Sair da party"));
        player.sendMessage(CC.translate(" &f• &6/party <nick> &7| Convidar um jogador para sua party"));
        player.sendMessage(CC.translate(" &f• &6/party join &8(&7player&8) &7| Entrar em uma party pública"));
        player.sendMessage(CC.translate(" &f• &6/party info &7| Ver informações da sua party"));
        player.sendMessage(CC.translate(" &f• &6/party chat &8(&7message&8) &7| Conversar com sua party"));
        player.sendMessage(CC.translate(" &f• &6/party accept &8(&7player&8) &7| Aceitar convite de party"));
        player.sendMessage(CC.translate(" &f• &6/party invite &8(&7player&8) &7| Convidar um jogador para sua party"));
        player.sendMessage(CC.translate(" &f• &6/party kick &8(&7player&8) &7| Expulsar jogador da sua party"));
        player.sendMessage(CC.translate(" &f• &6/party open &7| Abrir sua party ao público"));
        player.sendMessage(CC.translate(" &f• &6/party close &7| Fechar sua party ao público"));
        player.sendMessage(CC.translate(" &f• &6/party ban &8(&7player&8) &7| Banir jogador da sua party"));
        player.sendMessage(CC.translate(" &f• &6/party unban &8(&7player&8) &7| Desbanir jogador da sua party"));
        player.sendMessage(CC.translate(" &f• &6/party banlist &7| Listar banidos da sua party"));
        player.sendMessage(CC.translate(" &f• &6/party announce &8(&7message&8) &7| Convite público da sua party"));
        player.sendMessage(CC.translate(" &f• &6/party lookup &8(&7player&8) &7| Ver a party de um jogador"));
        player.sendMessage("");
    }

    private void handleDirectInvite(Player player, String targetName) {
        PartyService partyService = this.plugin.getService(PartyService.class);
        ProfileService profileService = this.plugin.getService(ProfileService.class);

        Player targetPlayer = Bukkit.getPlayer(targetName);
        if (targetPlayer == null) {
            player.sendMessage(CC.translate("&cO jogador que você está tentando convidar não está online."));
            return;
        }

        if (targetPlayer.equals(player)) {
            player.sendMessage(CC.translate("&cVocê não pode convidar a si mesmo para uma party."));
            return;
        }

        Party party = partyService.getPartyByMember(player.getUniqueId());
        if (party == null) {
            partyService.createParty(player);
            party = partyService.getPartyByMember(player.getUniqueId());
            if (party == null) {
                return;
            }
        }

        if (!party.getLeader().equals(player)) {
            player.sendMessage(CC.translate("&cVocê precisa ser o líder da party para convidar jogadores."));
            return;
        }

        Profile targetProfile = profileService.getProfile(targetPlayer.getUniqueId());
        if (targetProfile == null || targetProfile.getProfileData() == null || targetProfile.getProfileData().getSettingData() == null) {
            player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER)));
            return;
        }
        if (!targetProfile.getProfileData().getSettingData().isPartyInvitesEnabled()) {
            player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.ERROR_PLAYER_PARTY_INVITES_DISABLED)
                    .replace("{name-color}", String.valueOf(targetProfile.getNameColor()))
                    .replace("{player}", targetName)));
            return;
        }

        if (party.getMembers().contains(targetPlayer.getUniqueId())) {
            player.sendMessage(CC.translate("&6" + targetPlayer.getName() + " &cjá está na sua party."));
            return;
        }

        if (partyService.sendInvite(party, player, targetPlayer)) {
            party.notifyParty("&6" + targetPlayer.getName() + " &afoi convidado para a party por &6" + player.getName() + "&a.");
        }
    }
}
