package com.kaosmc.practice.feature.match.listener.types;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.feature.combat.CombatService;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.kit.setting.types.mechanic.KitSettingNoDamageImpl;
import com.kaosmc.practice.feature.kit.setting.types.mechanic.KitSettingNoFallDamageImpl;
import com.kaosmc.practice.feature.kit.setting.types.mode.KitSettingBoxing;
import com.kaosmc.practice.feature.kit.setting.types.mode.KitSettingHideAndSeek;
import com.kaosmc.practice.feature.kit.setting.types.mode.KitSettingSpleef;
import com.kaosmc.practice.feature.kit.setting.types.mode.KitSettingSumo;
import com.kaosmc.practice.feature.kit.setting.types.visual.KitSettingBowShotIndicator;
import com.kaosmc.practice.feature.kit.setting.types.visual.KitSettingHealthBar;
import com.kaosmc.practice.feature.match.Match;
import com.kaosmc.practice.feature.match.MatchState;
import com.kaosmc.practice.feature.match.internal.types.HideAndSeekMatch;
import com.kaosmc.practice.feature.match.model.internal.MatchGamePlayer;
import com.kaosmc.practice.feature.match.model.GameParticipant;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.enums.ProfileState;
import com.kaosmc.practice.adapter.core.CoreAdapter;
import com.kaosmc.practice.common.PlayerDisplayUtil;
import com.kaosmc.practice.common.reflect.ReflectionService;
import com.kaosmc.practice.common.reflect.internal.types.ActionBarReflectionServiceImpl;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.common.text.Symbol;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * @author Emmy
 * @project Kaos
 * @since 08/02/2025
 */
public class MatchDamageListener implements Listener {
    @EventHandler
    private void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile == null) {
            return;
        }

        if (profile.getState() == ProfileState.SPECTATING) event.setCancelled(true);
        if (profile.getState() == ProfileState.PLAYING) {
            Match match = profile.getMatch();
            if (match == null) {
                event.setCancelled(true);
                return;
            }

            MatchGamePlayer gamePlayer = match.getGamePlayer(player);
            if (gamePlayer == null) {
                event.setCancelled(true);
                return;
            }

            Kit matchKit = match.getKit();

            if (matchKit.isSettingEnabled(KitSettingNoFallDamageImpl.class)
                    && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
            }

            if (event.getCause() == EntityDamageEvent.DamageCause.FALL
                    && (matchKit.isSettingEnabled(KitSettingBoxing.class)
                    || matchKit.isSettingEnabled(KitSettingSumo.class)
                    || matchKit.isSettingEnabled(KitSettingSpleef.class))) {
                event.setCancelled(true);
            }

            if (match.getState() != MatchState.RUNNING) {
                event.setCancelled(true);
                return;
            }

            if (gamePlayer.isDead()) {
                event.setCancelled(true);
                return;
            }

            if (matchKit.isSettingEnabled(KitSettingBoxing.class)
                    || matchKit.isSettingEnabled(KitSettingSumo.class)
                    || matchKit.isSettingEnabled(KitSettingSpleef.class)
                    || matchKit.isSettingEnabled(KitSettingNoDamageImpl.class)) {
                event.setDamage(0);
                player.setNoDamageTicks(0);
                player.setMaximumNoDamageTicks(0);
                player.setHealth(player.getMaxHealth());
                player.updateInventory();
            }
        }
    }

    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player damaged = (Player) event.getEntity();
        Player attacker;

        if (event.getDamager() instanceof Player) {
            attacker = (Player) event.getDamager();
        } else if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.getShooter() instanceof Player) {
                attacker = (Player) projectile.getShooter();
            } else {
                return;
            }
        } else {
            return;
        }

        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);

        Profile damagedProfile = profileService.getProfile(damaged.getUniqueId());
        Profile attackerProfile = profileService.getProfile(attacker.getUniqueId());
        if (damagedProfile == null || attackerProfile == null) {
            event.setCancelled(true);
            return;
        }

        if (damagedProfile.getState() == ProfileState.SPECTATING || attackerProfile.getState() == ProfileState.SPECTATING) {
            event.setCancelled(true);
            return;
        }

        if (damagedProfile.getState() == ProfileState.PLAYING && attackerProfile.getState() == ProfileState.PLAYING) {
            Match match = damagedProfile.getMatch();
            if (match == null || attackerProfile.getMatch() != match) {
                event.setCancelled(true);
                return;
            }

            if (match.getState() != MatchState.RUNNING) {
                event.setCancelled(true);
                return;
            }

            MatchGamePlayer damagedGamePlayer = match.getGamePlayer(damaged);
            MatchGamePlayer attackerGamePlayer = match.getGamePlayer(attacker);

            if (damagedGamePlayer == null || attackerGamePlayer == null) {
                event.setCancelled(true);
                return;
            }

            if (damagedGamePlayer.isDead() || attackerGamePlayer.isDead()) {
                event.setCancelled(true);
                return;
            }

            if (!attacker.getUniqueId().equals(damaged.getUniqueId()) && match.isInSameTeam(attacker, damaged)) {
                if (match.getKit().isSettingEnabled(KitSettingHideAndSeek.class)) {
                    HideAndSeekMatch matchHideAndSeek = (HideAndSeekMatch) attackerProfile.getMatch();
                    GameParticipant<MatchGamePlayer> seekers = matchHideAndSeek.getParticipantA();

                    boolean isSeeker = seekers.containsPlayer(attacker.getUniqueId());

                    if (matchHideAndSeek.getGameEndTask() == null && isSeeker) {
                        return;
                    }
                }

                event.setCancelled(true);
                return;
            }

            if (!attacker.getUniqueId().equals(damaged.getUniqueId())) {
                attackerProfile.getMatch().getGamePlayer(attacker).getData().handleAttack();
                damagedProfile.getMatch().getGamePlayer(damaged).getData().resetCombo();

                GameParticipant<MatchGamePlayer> participant = match.getParticipant(attacker);
                GameParticipant<MatchGamePlayer> opponent = match.getParticipant(damaged);

                if (participant != null && opponent != null) {
                    participant.setTeamHits(participant.getTeamHits() + 1);

                    if (match.getKit().isSettingEnabled(KitSettingBowShotIndicator.class) && event.getDamager() instanceof Arrow) {
                        double finalHealth = damaged.getHealth() - event.getFinalDamage();
                        finalHealth = Math.max(0, finalHealth);

                        if (finalHealth > 0) {
                            String damagedName = PlayerDisplayUtil.resolveCurrentNick(damaged, damaged.getName());
                            attacker.sendMessage(CC.translate(KaosPractice.getInstance().getService(CoreAdapter.class).getCore().getPlayerColor(damaged) + damagedName + " &7&l" + Symbol.ARROW_R + " &6" + String.format("%.1f", finalHealth) + " &c" + Symbol.HEART));
                        }
                    }

                    if (match.getKit().isSettingEnabled(KitSettingBoxing.class)) {
                        int lowestPlayerCount = match.getParticipants().stream()
                                .mapToInt(p -> p.getPlayers().size())
                                .filter(size -> size > 0)
                                .min()
                                .orElse(1);

                        int requiredHits = lowestPlayerCount * 100;

                        if (participant.getTeamHits() >= requiredHits) {
                            opponent.getPlayers().forEach(matchGamePlayer -> {
                                Player loser = matchGamePlayer.getTeamPlayer();
                                if (loser == null) {
                                    return;
                                }

                                match.handleDeath(loser, EntityDamageEvent.DamageCause.ENTITY_ATTACK);
                            });
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onEntityDamageByEntityMonitor(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Profile profile = KaosPractice.getInstance().getService(ProfileService.class).getProfile(event.getEntity().getUniqueId());
            if (profile == null) {
                event.setCancelled(true);
                return;
            }

            if (profile.getState() == ProfileState.SPECTATING) {
                event.setCancelled(true);
                return;
            }

            if (profile.getState() == ProfileState.PLAYING) {
                if (profile.getMatch() == null) {
                    event.setCancelled(true);
                    return;
                }

                Player player = (Player) event.getEntity();
                Player attacker = (Player) event.getDamager();

                KaosPractice.getInstance().getService(CombatService.class).setLastAttacker(player, attacker);

                if (profile.getMatch().getKit().isSettingEnabled(KitSettingHealthBar.class)) {
                    KaosPractice.getInstance().getService(ReflectionService.class).getReflectionService(ActionBarReflectionServiceImpl.class).visualizeTargetHealth(attacker, player);
                }
            }
        }
    }
}
