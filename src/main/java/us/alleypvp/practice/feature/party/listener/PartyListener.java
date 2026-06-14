package us.alleypvp.practice.feature.party.listener;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.core.profile.enums.ChatChannel;
import us.alleypvp.practice.feature.party.PartyService;
import us.alleypvp.practice.feature.party.Party;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Remi
 * @project Alley
 * @date 5/25/2024
 */
public class PartyListener implements Listener {
    @EventHandler
    private void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);
        PartyService partyService = AlleyPractice.getInstance().getService(PartyService.class);

        if (profileService == null || partyService == null) {
            return;
        }

        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile == null || profile.getProfileData() == null || profile.getProfileData().getSettingData() == null) {
            return;
        }

        boolean forcedPartyMessage = event.getMessage().startsWith("#") || event.getMessage().startsWith("!");
        boolean inPartyChannel = ChatChannel.PARTY.toString().equalsIgnoreCase(profile.getProfileData().getSettingData().getChatChannel());
        if (!forcedPartyMessage && !inPartyChannel) {
            return;
        }

        Party party = profile.getParty();
        if (party == null
                || party.getMembers() == null
                || party.getMembers().isEmpty()
                || !party.getMembers().contains(player.getUniqueId())) {
            profile.getProfileData().getSettingData().setChatChannel(ChatChannel.GLOBAL.toString());
            player.sendMessage(CC.translate("&eSeu chat voltou para o global, pois você não está em uma party ativa."));
            event.setCancelled(true);
            return;
        }

        String rawMessage = event.getMessage();
        if (forcedPartyMessage && rawMessage.length() > 1) {
            rawMessage = rawMessage.substring(1);
        }
        if (rawMessage.trim().isEmpty()) {
            event.setCancelled(true);
            return;
        }

        String partyMessage = partyService.formatPartyChatMessage(player, rawMessage);
        party.notifyParty(partyMessage);
        event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile == null) {
            return;
        }

        Party party = profile.getParty();
        if (party == null) {
            return;
        }

        if (party.isLeader(player)) {
            AlleyPractice.getInstance().getService(PartyService.class).disbandParty(player);
            return;
        }

        AlleyPractice.getInstance().getService(PartyService.class).leaveParty(player);
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile == null) {
            return;
        }

        Party party = profile.getParty();
        if (party == null) {
            return;
        }

        if (party.isLeader(player)) {
            AlleyPractice.getInstance().getService(PartyService.class).disbandParty(player);
            return;
        }

        AlleyPractice.getInstance().getService(PartyService.class).leaveParty(player);
    }
}
