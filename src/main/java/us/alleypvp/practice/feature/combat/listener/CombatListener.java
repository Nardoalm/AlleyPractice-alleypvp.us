package us.alleypvp.practice.feature.combat.listener;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.feature.combat.CombatService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Emmy
 * @project Alley
 * @since 14/03/2025
 */
public class CombatListener implements Listener {
    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        this.removeFromCombatMap(player);
    }

    @EventHandler
    private void onKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        this.removeFromCombatMap(player);
    }

    /**
     * Removes the player from the combat map.
     *
     * @param player The player to remove.
     */
    private void removeFromCombatMap(Player player) {
        CombatService combatService = AlleyPractice.getInstance().getService(CombatService.class);
        if (combatService.getCombatMap().containsKey(player.getUniqueId())) {
            combatService.removeLastAttacker(player, true);
        }
    }
}