package com.kaosmc.practice.feature.match.listener.types;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.feature.match.Match;
import com.kaosmc.practice.feature.match.MatchState;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.enums.ProfileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Emmy
 * @project Kaos
 * @since 08/02/2025
 */
public class MatchDisconnectListener implements Listener {
    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() == ProfileState.PLAYING || profile.getState() == ProfileState.SPECTATING) {
            profile.setState(ProfileState.LOBBY);
            Match match = profile.getMatch();
            if (match.getSpectators().contains(player.getUniqueId())) {
                match.removeSpectator(player, true);
            }

            if (match.getGamePlayer(player) == null) {
                return;
            }

            if (match.getState() == MatchState.STARTING || match.getState() == MatchState.RUNNING) {
                match.handleDisconnect(player);
            }
        }
    }

    @EventHandler
    private void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() == ProfileState.PLAYING) {
            Match match = profile.getMatch();

            if (match.getState() == MatchState.STARTING || match.getState() == MatchState.RUNNING) {
                match.handleDisconnect(player);
            }
        }
    }
}