package us.alleypvp.practice.feature.abilities.types;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.feature.abilities.Ability;
import us.alleypvp.practice.feature.abilities.AbilityService;
import us.alleypvp.practice.common.time.DurationFormatter;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.enums.GlobalCooldown;
import us.alleypvp.practice.common.PlayerUtil;
import us.alleypvp.practice.common.text.CC;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class Rocket extends Ability {

    public Rocket() {
        super("ROCKET");
    }

    @EventHandler
    public void onItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);
        AbilityService abilityService = AlleyPractice.getInstance().getService(AbilityService.class);

        Profile profile = profileService.getProfile(player.getUniqueId());

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!isAbility(player.getItemInHand())) {
                return;
            }

            if (profile.getCooldown(Rocket.class).onCooldown(player)) {
                player.sendMessage(CC.translate("&fVocê está no cooldown de &b&lRocket &7por &4" + DurationFormatter.getRemaining(profile.getCooldown(Rocket.class).getRemainingMillis(player), true, true)));
                player.updateInventory();
                event.setCancelled(true);
                return;
            }

            if (profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).onCooldown(player)) {
                player.sendMessage(CC.translate("&fVocê está no cooldown de &b&lPartner Item &fpor &b" + DurationFormatter.getRemaining(profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).getRemainingMillis(player), true, true)));
                player.updateInventory();
                event.setCancelled(true);
                return;
            }

            if (isAbility(player.getItemInHand())) {
                player.setVelocity(new Vector(0.1D, 2.0D, 0.0D));

                PlayerUtil.decrement(player);

                profile.getCooldown(Rocket.class).applyCooldown(player, 60 * 1000);
                profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).applyCooldown(player, 10 * 1000);

                player.setMetadata("rocket", new FixedMetadataValue(AlleyPractice.getInstance(), true));
            }
        }
    }

    @EventHandler
    public void checkCooldown(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        Profile profile = AlleyPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
            if (!isAbility(player.getItemInHand())) {
                return;
            }
            if (isAbility(player.getItemInHand())) {
                if (this.hasCooldown(player)) {
                    player.sendMessage(CC.translate("&fVocê está em cooldown por &4" + DurationFormatter.getRemaining(profile.getCooldown(Rocket.class).getRemainingMillis(player), true)));
                    event.setCancelled(true);
                    player.updateInventory();
                }
            }
        }
    }

    @EventHandler
    public void fallDamage(final EntityDamageEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            final Player player = (Player) event.getEntity();
            Profile profile = AlleyPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
            if (profile.getCooldown(Rocket.class).onCooldown(player)) {
                event.setCancelled(true);
            }
        }
    }
}
