package us.alleypvp.practice.feature.queue.listener;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.enums.ProfileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Remi
 * @project Alley
 * @date 5/25/2024
 */
public class QueueListener implements Listener {
    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() == ProfileState.WAITING
                && profile.getQueueProfile() != null
                && profile.getQueueProfile().getQueue() != null) {
            profile.getQueueProfile().getQueue().removePlayer(profile.getQueueProfile());
        }
    }

    @EventHandler
    private void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() == ProfileState.WAITING
                && profile.getQueueProfile() != null
                && profile.getQueueProfile().getQueue() != null) {
            profile.getQueueProfile().getQueue().removePlayer(profile.getQueueProfile());
        }
    }
}
