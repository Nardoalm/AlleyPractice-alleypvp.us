package com.kaosmc.practice.feature.party.listener;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.feature.party.PartyService;
import com.kaosmc.practice.feature.party.Party;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.enums.ChatChannel;
import com.kaosmc.practice.common.text.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Remi
 * @project Kaos
 * @date 5/25/2024
 */
public class PartyListener implements Listener {
    @EventHandler
    private void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        PartyService partyService = KaosPractice.getInstance().getService(PartyService.class);

        Profile profile = profileService.getProfile(event.getPlayer().getUniqueId());

        if (profile.getProfileData().getSettingData().getChatChannel().equalsIgnoreCase(ChatChannel.PARTY.toString())) {
            if (profile.getParty() == null) {
                player.sendMessage(CC.translate("&cVocê não está em uma party."));
                event.setCancelled(true);
                return;
            }

            if (!profile.getProfileData().getSettingData().isPartyMessagesEnabled()) {
                player.sendMessage(CC.translate("&cVocê está com as mensagens da party desativadas."));
                event.setCancelled(true);
                return;
            }

            String partyMessage = partyService.getChatFormat().replace("{player}", player.getName()).replace("{message}", event.getMessage());
            profile.getParty().notifyParty(partyMessage);
            event.setCancelled(true);
            return;
        }

        if (event.getMessage().startsWith("#") || event.getMessage().startsWith("!")) {
            if (profile.getParty() == null) {
                player.sendMessage(CC.translate("&cVocê não está em uma party."));
                event.setCancelled(true);
                return;
            }

            if (!profile.getProfileData().getSettingData().isPartyMessagesEnabled()) {
                player.sendMessage(CC.translate("&cVocê está com as mensagens da party desativadas."));
                event.setCancelled(true);
                return;
            }

            String partyMessage = partyService.getChatFormat().replace("{player}", player.getName()).replace("{message}", event.getMessage().substring(1));
            profile.getParty().notifyParty(partyMessage);
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        Party party = profile.getParty();
        if (party == null) {
            return;
        }

        if (party.getLeader() == player) {
            KaosPractice.getInstance().getService(PartyService.class).disbandParty(player);
            return;
        }

        KaosPractice.getInstance().getService(PartyService.class).leaveParty(player);
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        Party party = profile.getParty();
        if (party == null) {
            return;
        }

        if (party.getLeader() == player) {
            KaosPractice.getInstance().getService(PartyService.class).disbandParty(player);
            return;
        }

        KaosPractice.getInstance().getService(PartyService.class).leaveParty(player);
    }
}