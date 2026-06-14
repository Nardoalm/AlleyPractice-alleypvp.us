package us.alleypvp.practice.feature.ffa.listener;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.common.PlayerDisplayUtil;
import us.alleypvp.practice.feature.combat.CombatService;
import us.alleypvp.practice.feature.kit.setting.types.visual.KitSettingBowShotIndicator;
import us.alleypvp.practice.feature.kit.setting.types.visual.KitSettingHealthBar;
import us.alleypvp.practice.feature.ffa.spawn.FFASpawnService;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.enums.ProfileState;
import us.alleypvp.practice.common.reflect.ReflectionService;
import us.alleypvp.practice.common.reflect.internal.types.ActionBarReflectionServiceImpl;
import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.common.text.Symbol;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * @author Emmy
 * @project kaos-practice
 * @since 16/07/2025
 */
public class FFADamageListener implements Listener {
    /**
     * Handles the EntityDamageByEntityEvent.
     * The event is cancelled if the player is in the FFA state and tries to damage another player.
     *
     * @param event The EntityDamageByEntityEvent
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntityMonitor(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.FFA) return;

        CombatService combatService = AlleyPractice.getInstance().getService(CombatService.class);
        combatService.setLastAttacker(player, attacker);

        if (profile.getFfaMatch().getKit().isSettingEnabled(KitSettingHealthBar.class)) {
            AlleyPractice.getInstance().getService(ReflectionService.class).getReflectionService(ActionBarReflectionServiceImpl.class).visualizeTargetHealth(attacker, player);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player attacker;

        if (event.getDamager() instanceof Player) {
            attacker = (Player) event.getDamager();
        } else if (event.getDamager() instanceof Projectile) {
            if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
                attacker = (Player) ((Projectile) event.getDamager()).getShooter();
            } else {
                return;
            }
        } else {
            return;
        }

        if (!(event.getEntity() instanceof Player)) return;
        Player victim = (Player) event.getEntity();

        ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(victim.getUniqueId());
        if (profile.getState() != ProfileState.FFA) {
            return;
        }

        if (victim != attacker) {
            if (profile.getFfaMatch().getKit().isSettingEnabled(KitSettingBowShotIndicator.class) && event.getDamager() instanceof Arrow) {
                double finalHealth = victim.getHealth() - event.getFinalDamage();
                finalHealth = Math.max(0, finalHealth);

                if (finalHealth > 0) {
                    String victimName = PlayerDisplayUtil.resolveCurrentNick(victim, victim.getName());
                    attacker.sendMessage(CC.translate(profile.getNameColor() + victimName + " &7&l" + Symbol.ARROW_R + " &b" + String.format("%.1f", finalHealth) + " &c" + Symbol.HEART));
                }
            }
        }

        FFASpawnService ffaSpawnService = AlleyPractice.getInstance().getService(FFASpawnService.class);
        if ((ffaSpawnService.getCuboid().isIn(victim) && ffaSpawnService.getCuboid().isIn(attacker)) || (!ffaSpawnService.getCuboid().isIn(victim) && ffaSpawnService.getCuboid().isIn(attacker)) || (ffaSpawnService.getCuboid().isIn(victim) && !ffaSpawnService.getCuboid().isIn(attacker))) {
            CombatService combatService = AlleyPractice.getInstance().getService(CombatService.class);
            if (combatService.isPlayerInCombat(victim.getUniqueId()) && combatService.isPlayerInCombat(attacker.getUniqueId())) {
                return;
            }

            event.setCancelled(true);
            return;
        }

        if (victim != attacker) {
            CombatService combatService = AlleyPractice.getInstance().getService(CombatService.class);
            combatService.setLastAttacker(victim, attacker);
        }
    }
}
