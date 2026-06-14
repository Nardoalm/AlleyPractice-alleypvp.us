package us.alleypvp.practice.feature.tournament.player.internal;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.bootstrap.annotation.Service;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.enums.ProfileState;
import us.alleypvp.practice.feature.hotbar.HotbarService;
import us.alleypvp.practice.feature.tournament.model.Tournament;
import us.alleypvp.practice.feature.tournament.player.PlayerTournamentStateService;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @date 8/08/2025
 */

@Service(provides = PlayerTournamentStateService.class, priority = 1100)
public class PlayerTournamentStateServiceImpl implements PlayerTournamentStateService {
    private final ProfileService profileService;
    private final HotbarService hotbarService;

    public PlayerTournamentStateServiceImpl() {
        this.profileService = AlleyPractice.getInstance().getService(ProfileService.class);
        this.hotbarService = AlleyPractice.getInstance().getService(HotbarService.class);
    }

    @Override
    public void setPlayerTournamentState(Player player, Tournament tournament) {
        if (player == null || !player.isOnline()) return;

        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile == null) return;

        profile.setState(ProfileState.TOURNAMENT_LOBBY);
        profile.setTournament(tournament);
        hotbarService.applyHotbarItems(player);
    }

    @Override
    public void resetPlayerStateToLobby(Player player) {
        if (player == null || !player.isOnline()) return;

        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile == null) return;

        profile.setState(ProfileState.LOBBY);
        profile.setTournament(null);
        hotbarService.applyHotbarItems(player);
    }
}