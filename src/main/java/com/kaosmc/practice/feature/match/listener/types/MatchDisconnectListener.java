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
        this.handleDisconnect(event.getPlayer());
    }

    @EventHandler
    private void onPlayerKick(PlayerKickEvent event) {
        this.handleDisconnect(event.getPlayer());
    }

    private void handleDisconnect(Player player) {
        if (player == null) {
            return;
        }

        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile == null) {
            return;
        }

        if (profile.getState() != ProfileState.PLAYING && profile.getState() != ProfileState.SPECTATING) {
            return;
        }

        Match match = profile.getMatch();
        if (match == null) {
            profile.setState(ProfileState.LOBBY);
            return;
        }

        if (match.getSpectators().contains(player.getUniqueId())) {
            match.removeSpectator(player, true);
            return;
        }

        if (match.getGamePlayer(player) == null || match.getGamePlayer(player).isDisconnected()) {
            return;
        }

        if (match.getState() == MatchState.STARTING || match.getState() == MatchState.RUNNING) {
            match.handleDisconnect(player);
            profile.setState(ProfileState.LOBBY);
            profile.setMatch(null);
        }
    }
}
