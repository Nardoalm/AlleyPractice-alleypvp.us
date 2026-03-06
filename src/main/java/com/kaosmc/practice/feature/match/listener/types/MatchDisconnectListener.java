package com.kaosmc.practice.feature.match.listener.types;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.feature.match.Match;
import com.kaosmc.practice.feature.match.MatchState;
import com.kaosmc.practice.feature.match.MatchService;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.enums.ProfileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Emmy
 * @project Kaos
 * @since 08/02/2025
 */
public class MatchDisconnectListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    private void onPlayerQuit(PlayerQuitEvent event) {
        this.handleDisconnect(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onPlayerKick(PlayerKickEvent event) {
        this.handleDisconnect(event.getPlayer());
    }

    private void handleDisconnect(Player player) {
        if (player == null) {
            return;
        }

        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfiles().get(player.getUniqueId());
        Match match = profile != null ? profile.getMatch() : this.findActiveMatchByPlayer(player);
        if (match == null) {
            if (profile != null) {
                profile.setState(ProfileState.LOBBY);
                profile.setMatch(null);
            }
            return;
        }

        if (match.getSpectators().contains(player.getUniqueId())) {
            match.removeSpectator(player, true);
            if (profile != null) {
                profile.setState(ProfileState.LOBBY);
                profile.setMatch(null);
            }
            return;
        }

        if (match.getGamePlayer(player) == null || match.getGamePlayer(player).isDisconnected()) {
            if (profile != null) {
                profile.setState(ProfileState.LOBBY);
                profile.setMatch(null);
            }
            return;
        }

        if (match.getState() == MatchState.STARTING || match.getState() == MatchState.RUNNING) {
            match.handleDisconnect(player);
        }

        if (profile != null) {
            profile.setState(ProfileState.LOBBY);
            profile.setMatch(null);
        }
    }

    private Match findActiveMatchByPlayer(Player player) {
        if (player == null) {
            return null;
        }

        MatchService matchService = KaosPractice.getInstance().getService(MatchService.class);
        if (matchService == null) {
            return null;
        }

        return matchService.getMatches().stream()
                .filter(match -> match != null
                        && (match.getGamePlayer(player) != null || match.getSpectators().contains(player.getUniqueId())))
                .findFirst()
                .orElse(null);
    }
}
