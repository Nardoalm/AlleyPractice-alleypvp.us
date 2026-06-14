package us.alleypvp.practice.feature.visibility.internal;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.bootstrap.annotation.Service;
import us.alleypvp.practice.common.logger.Logger;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.enums.ProfileState;
import us.alleypvp.practice.feature.event.EventService;
import us.alleypvp.practice.feature.match.model.internal.MatchGamePlayer;
import us.alleypvp.practice.feature.visibility.VisibilityService;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 06/06/2025
 */
@Service(provides = VisibilityService.class, priority = 360)
public class VisibilityServiceImpl implements VisibilityService {
    private final AlleyPractice plugin;
    private final ProfileService profileService;

    public VisibilityServiceImpl(AlleyPractice plugin, ProfileService profileService) {
        this.plugin = plugin;
        this.profileService = profileService;
    }

    @Override
    public void updateVisibility(Player player) {
        if (player == null || !player.isOnline()) return;

        for (Player otherPlayer : this.plugin.getServer().getOnlinePlayers()) {
            if (player.equals(otherPlayer)) continue;
            handleVisibility(player, otherPlayer);
            handleVisibility(otherPlayer, player);
        }
    }

    public void handleVisibility(Player viewer, Player target) {
        if (viewer == null || target == null || !viewer.isOnline() || !target.isOnline()) {
            return;
        }

        Profile viewerProfile = this.profileService.getProfile(viewer.getUniqueId());
        Profile targetProfile = this.profileService.getProfile(target.getUniqueId());

        if (viewerProfile == null || targetProfile == null) {
            Logger.error("Não foi possível obter o perfil do visualizador ou do jogador alvo.");
            return;
        }

        ProfileState viewerProfileState = viewerProfile.getState();
        ProfileState targetProfileState = targetProfile.getState();

        switch (viewerProfileState) {
            case LOBBY:
            case WAITING:
                this.handleLobbyAndQueueState(viewer, target, targetProfileState);
                break;
            case FFA:
                this.handleFfaState(viewer, target, viewerProfile, targetProfile);
                break;
            case PLAYING:
                this.handlePlayingCase(viewer, target, viewerProfile, targetProfile);
                break;
            case PLAYING_EVENT:
                this.handleEventCase(viewer, target);
                break;
            case SPECTATING:
                this.handleSpectatingCase(viewer, target, viewerProfile);
                break;
            default:
                viewer.showPlayer(target);
                break;
        }
    }

    private void handleLobbyAndQueueState(Player viewer, Player target, ProfileState targetProfileState) {
        switch (targetProfileState) {
            case LOBBY:
            case WAITING:
            case EDITING:
                viewer.showPlayer(target);
                break;
            default:
                viewer.hidePlayer(target);
                break;
        }
    }

    private void handleFfaState(Player viewer, Player target, Profile viewerProfile, Profile targetProfile) {
        if (targetProfile.getState() != ProfileState.FFA) {
            viewer.hidePlayer(target);
            return;
        }

        if (viewerProfile.getFfaMatch().getKit() == targetProfile.getFfaMatch().getKit()) {
            viewer.showPlayer(target);
        } else {
            viewer.hidePlayer(target);
        }
    }

    private void handlePlayingCase(Player viewer, Player target, Profile viewerProfile, Profile targetProfile) {
        if (viewerProfile.getMatch() == null) {
            viewer.hidePlayer(target);
            return;
        }

        if (targetProfile.getState() == ProfileState.SPECTATING) {
            viewer.hidePlayer(target);
            return;
        }

        if (targetProfile.getMatch() == null || !viewerProfile.getMatch().equals(targetProfile.getMatch())) {
            viewer.hidePlayer(target);
            return;
        }

        MatchGamePlayer targetGamePlayer = viewerProfile.getMatch().getGamePlayer(target);
        if (targetProfile.getMatch().getSpectators().contains(target.getUniqueId())) {
            viewer.hidePlayer(target);
            return;
        }

        if (targetGamePlayer.isEliminated()) {
            viewer.hidePlayer(target);
            return;
        }

        viewer.showPlayer(target);
    }

    private void handleSpectatingCase(Player viewer, Player target, Profile viewerProfile) {
        Profile targetProfile = this.profileService.getProfile(target.getUniqueId());
        if (targetProfile.getState() == ProfileState.SPECTATING) {
            viewer.showPlayer(target);
            return;
        }

        if (viewerProfile.getMatch() == null) {
            viewer.hidePlayer(target);
            return;
        }

        MatchGamePlayer spectatingTargetGamePlayer = viewerProfile.getMatch().getGamePlayer(target);
        if (spectatingTargetGamePlayer == null) {
            viewer.hidePlayer(target);
            return;
        }

        viewer.showPlayer(target);
    }

    private void handleEventCase(Player viewer, Player target) {
        EventService eventService = this.plugin.getService(EventService.class);
        if (eventService == null) {
            viewer.hidePlayer(target);
            return;
        }

        if (eventService.isEventParticipant(viewer.getUniqueId()) && eventService.isEventParticipant(target.getUniqueId())) {
            viewer.showPlayer(target);
            return;
        }

        viewer.hidePlayer(target);
    }
}