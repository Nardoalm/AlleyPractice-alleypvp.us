package us.alleypvp.practice.feature.item.listener;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.core.locale.LocaleService;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.feature.cooldown.Cooldown;
import us.alleypvp.practice.feature.cooldown.CooldownService;
import us.alleypvp.practice.feature.cooldown.CooldownType;
import us.alleypvp.practice.feature.item.ItemService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * @author Emmy
 * @project kaos-practice
 * @since 18/07/2025
 */
public class ItemListener implements Listener {

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = AlleyPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        if (profile.getMatch() == null && profile.getFfaMatch() == null) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemService itemService = AlleyPractice.getInstance().getService(ItemService.class);
        if (item.isSimilar(itemService.getGoldenHead())) {
            event.setCancelled(true);

            if (this.isOnHeadCooldown(player)) return;

            itemService.performHeadConsume(player, item);
        }
    }

    /**
     * Checks if the player is on cooldown for consuming a golden head.
     *
     * @param player The player to check the cooldown for.
     * @return true if the player is on cooldown, false otherwise.
     */
    private boolean isOnHeadCooldown(Player player) {
        CooldownService cooldownService = AlleyPractice.getInstance().getService(CooldownService.class);
        LocaleService localeService = AlleyPractice.getInstance().getService(LocaleService.class);

        CooldownType cooldownType = CooldownType.GOLDEN_HEAD_CONSUME;
        Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownService.getCooldown(player.getUniqueId(), cooldownType));
        if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
            player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.COOLDOWN_FIREBALL_MUST_WAIT));
            return true;
        }

        Cooldown cooldown = optionalCooldown.orElseGet(() -> {
            Cooldown newCooldown = new Cooldown(cooldownType, () -> {
            });
            cooldownService.addCooldown(player.getUniqueId(), cooldownType, newCooldown);
            return newCooldown;
        });

        cooldown.resetCooldown();
        return false;
    }
}