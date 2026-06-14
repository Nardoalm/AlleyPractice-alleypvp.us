package us.alleypvp.practice.feature.ffa.listener;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.feature.combat.CombatService;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.enums.ProfileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Emmy
 * @project kaos-practice
 * @since 16/07/2025
 */
public class FFADisconnectListener implements Listener {
    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.FFA) return;

        CombatService combatService = AlleyPractice.getInstance().getService(CombatService.class);
        if (combatService.isPlayerInCombat(player.getUniqueId())) {
            profile.getFfaMatch().handleCombatLog(player, combatService.getLastAttacker(player));
        }

        profile.getFfaMatch().leave(player);
    }

    @EventHandler
    private void onKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.FFA) return;

        CombatService combatService = AlleyPractice.getInstance().getService(CombatService.class);
        if (combatService.isPlayerInCombat(player.getUniqueId())) {
            profile.getFfaMatch().handleCombatLog(player, combatService.getLastAttacker(player));
        }

        profile.getFfaMatch().leave(player);
    }
}