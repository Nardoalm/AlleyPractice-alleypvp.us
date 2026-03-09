package com.kaosmc.practice.feature.abilities.types;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.feature.abilities.Ability;
import com.kaosmc.practice.feature.abilities.AbilityService;
import com.kaosmc.practice.common.time.DurationFormatter;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.enums.GlobalCooldown;
import com.kaosmc.practice.common.text.CC;
import org.bukkit.Location;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class Switcher extends Ability {
    private final KaosPractice plugin = KaosPractice.getInstance();

    public Switcher() {
        super("SWITCHER");
    }

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player shooter = (Player) event.getEntity().getShooter();
            if (isAbility(shooter.getItemInHand())) {
                event.getEntity().setMetadata(this.getAbility(), new FixedMetadataValue(this.plugin, true));
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!isAbility(event.getItem())) return;

        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player shooter = event.getPlayer();

            ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
            AbilityService abilityService = KaosPractice.getInstance().getService(AbilityService.class);

            Profile profile = profileService.getProfile(shooter.getUniqueId());

            if (profile.getCooldown(Switcher.class).onCooldown(shooter)) {
                shooter.sendMessage(CC.translate("&fVocê está no cooldown de &6&lSwitcher &7por &4" + DurationFormatter.getRemaining(profile.getCooldown(Switcher.class).getRemainingMillis(shooter), true, true)));
                shooter.updateInventory();
                event.setCancelled(true);
                return;
            }

            if(profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).onCooldown(shooter)){
                shooter.sendMessage(CC.translate("&fVocê está no cooldown de &6&lPartner Item &fpor &6" + DurationFormatter.getRemaining(profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).getRemainingMillis(shooter), true, true)));
                shooter.updateInventory();
                event.setCancelled(true);
                return;
            }

            profile.getCooldown(Switcher.class).applyCooldown(shooter, 8 * 1000);
            profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).applyCooldown(shooter,  10 * 1000);

            abilityService.sendCooldownExpiredMessage(shooter, this.getName(), this.getAbility());
            abilityService.sendPlayerMessage(shooter, this.getAbility());
        }
    }

    @EventHandler
    public void checkCooldown(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        Profile profile = KaosPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
            if (!isAbility(player.getItemInHand())) {
                return;
            }
            if (isAbility(player.getItemInHand())) {
                if (this.hasCooldown(player)) {
                    player.sendMessage(CC.translate("&fVocê está em cooldown por &4" + DurationFormatter.getRemaining(profile.getCooldown(Switcher.class).getRemainingMillis(player), true)));
                    event.setCancelled(true);
                    player.updateInventory();
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();

            if (projectile instanceof Egg && projectile.hasMetadata(this.getAbility())) {
                Player player = (Player) event.getEntity();
                if (!(projectile.getShooter() instanceof Player)) {
                    return;
                }
                Player shooter = (Player) projectile.getShooter();

                Location playerLocation = player.getLocation().clone();
                Location shooterLocation = shooter.getLocation().clone();

                player.teleport(shooterLocation);
                shooter.teleport(playerLocation);

                KaosPractice.getInstance().getService(AbilityService.class).sendTargetMessage(player, shooter, this.getAbility());
            }
            else if (projectile instanceof Snowball && projectile.hasMetadata(this.getAbility())) {
                Player player = (Player) event.getEntity();
                if (!(projectile.getShooter() instanceof Player)) {
                    return;
                }
                Player shooter = (Player) projectile.getShooter();

                Location playerLocation = player.getLocation().clone();
                Location shooterLocation = shooter.getLocation().clone();

                player.teleport(shooterLocation);
                shooter.teleport(playerLocation);

                KaosPractice.getInstance().getService(AbilityService.class).sendTargetMessage(player, shooter, this.getAbility());
            }
        }
    }
}
