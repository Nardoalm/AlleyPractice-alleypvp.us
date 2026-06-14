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

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/15/2023
 */
public class LuckyIngot extends Ability {
    private final AlleyPractice plugin = AlleyPractice.getInstance();

    public LuckyIngot() {
        super("LUCKY_INGOT");
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

            if (profile.getCooldown(LuckyIngot.class).onCooldown(player)) {
                player.sendMessage(CC.translate("&fVocê está no cooldown de &b&lLucky Ingot &7por &4" + DurationFormatter.getRemaining(profile.getCooldown(LuckyIngot.class).getRemainingMillis(player), true, true)));
                player.updateInventory();
                return;
            }

            if(profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).onCooldown(player)){
                player.sendMessage(CC.translate("&fVocê está no cooldown de &b&lPartner Item &fpor &b" + DurationFormatter.getRemaining(profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).getRemainingMillis(player), true, true)));
                player.updateInventory();
                return;
            }

            PlayerUtil.decrement(player);

            profile.getCooldown(LuckyIngot.class).applyCooldown(player, 60 * 1000);
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
                    player.sendMessage(CC.translate("&fVocê está em cooldown por &4" + DurationFormatter.getRemaining(profile.getCooldown(LuckyIngot.class).getRemainingMillis(player), true)));
                    event.setCancelled(true);
                    player.updateInventory();
                }
            }
        }
    }

    private void giveRandomEffect(Player player) {
        switch (ThreadLocalRandom.current().nextInt(2)) {
            case 0:
                player.removePotionEffect(PotionEffectType.REGENERATION);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 11, 1));

                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 11, 1));
                break;
            case 1:
                player.removePotionEffect(PotionEffectType.WEAKNESS);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 11, 1));

                player.removePotionEffect(PotionEffectType.WITHER);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 11, 1));

        }
    }
}
