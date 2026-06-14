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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ThreadLocalRandom;

public class PocketBard extends Ability {
    private final AlleyPractice plugin = AlleyPractice.getInstance();

    public PocketBard() {
        super("POCKET_BARD");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!isAbility(event.getItem())) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);

            Player player = event.getPlayer();

            ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);
            AbilityService abilityService = AlleyPractice.getInstance().getService(AbilityService.class);

            Profile profile = profileService.getProfile(player.getUniqueId());

            if (profile.getCooldown(PocketBard.class).onCooldown(player)) {
                player.sendMessage(CC.translate("&fVocê está no cooldown de &b&lPocket Bard &7por &4" + DurationFormatter.getRemaining(profile.getCooldown(PocketBard.class).getRemainingMillis(player), true, true)));
                player.updateInventory();
                return;
            }

            if(profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).onCooldown(player)){
                player.sendMessage(CC.translate("&fVocê está no cooldown de &b&lPartner Item &fpor &b" + DurationFormatter.getRemaining(profile.getCooldown(PocketBard.class).getRemainingMillis(player), true, true)));
                player.updateInventory();
                return;
            }

            PlayerUtil.decrement(player);

            profile.getCooldown(PocketBard.class).applyCooldown(player, 60 * 1000);
            profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).applyCooldown(player,  10 * 1000);

            this.giveRandomEffect(player);

            abilityService.sendCooldownExpiredMessage(player, this.getName(), this.getAbility());
            abilityService.sendPlayerMessage(player, this.getAbility());
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
                    player.sendMessage(CC.translate("&fVocê está em cooldown por &4" + DurationFormatter.getRemaining(profile.getCooldown(PocketBard.class).getRemainingMillis(player), true)));
                    event.setCancelled(true);
                    player.updateInventory();
                }
            }
        }
    }

    private void giveRandomEffect(Player player) {
        switch (ThreadLocalRandom.current().nextInt(3)) {
            case 0:
                player.removePotionEffect(PotionEffectType.REGENERATION);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 11, 1));
                break;
            case 1:
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 11, 2));
                break;
            case 2:
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 11, 1));
                break;
        }
    }
}
