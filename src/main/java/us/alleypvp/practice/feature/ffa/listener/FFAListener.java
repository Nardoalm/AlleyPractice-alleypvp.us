package us.alleypvp.practice.feature.ffa.listener;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.common.InventoryUtil;
import us.alleypvp.practice.common.ListenerUtil;
import us.alleypvp.practice.core.locale.LocaleService;
import us.alleypvp.practice.core.locale.internal.impl.message.GameMessagesLocaleImpl;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.enums.ProfileState;
import us.alleypvp.practice.feature.combat.CombatService;
import us.alleypvp.practice.feature.cooldown.Cooldown;
import us.alleypvp.practice.feature.cooldown.CooldownService;
import us.alleypvp.practice.feature.cooldown.CooldownType;
import us.alleypvp.practice.feature.ffa.spawn.FFASpawnService;
import us.alleypvp.practice.feature.kit.setting.types.mechanic.KitSettingNoHungerImpl;
import us.alleypvp.practice.feature.kit.setting.types.mechanic.KitSettingSoup;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 14:24
 */
public class FFAListener implements Listener {

    @EventHandler
    private void onPlayerDropItem(PlayerDropItemEvent event) {
        ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);
        LocaleService localeService = AlleyPractice.getInstance().getService(LocaleService.class);

        Player player = event.getPlayer();
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.FFA) return;

        if (ListenerUtil.isSword(event.getItemDrop().getItemStack().getType())) {
            event.setCancelled(true);
            player.sendMessage(localeService.getString(GameMessagesLocaleImpl.GAME_CANNOT_DROP_SWORD));
            return;
        }

        ListenerUtil.clearDroppedItemsOnRegularItemDrop(event.getItemDrop());
    }

    @EventHandler
    private void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.FFA) {
            return;
        }

        ItemStack item = event.getItem();
        if (item.getType() == Material.POTION) {
            AlleyPractice.getInstance().getServer().getScheduler().runTaskLater(AlleyPractice.getInstance(), () -> {
                player.getInventory().removeItem(new ItemStack(Material.GLASS_BOTTLE, 1));
                player.updateInventory();
            }, 1L);
        }
    }

    @EventHandler
    private void onSoupUse(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.MUSHROOM_SOUP) {
            return;
        }

        Player player = event.getPlayer();
        Profile profile = AlleyPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.FFA || profile.getFfaMatch() == null) {
            return;
        }

        if (!profile.getFfaMatch().getKit().isSettingEnabled(KitSettingSoup.class)) {
            return;
        }

        double maxHealth = player.getMaxHealth();
        if (player.getHealth() >= maxHealth) {
            return;
        }

        event.setCancelled(true);
        player.setHealth(Math.min(maxHealth, player.getHealth() + 7.0D));
        player.setItemInHand(new ItemStack(Material.BOWL));
        player.updateInventory();
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.FFA) return;
        event.setDeathMessage(null);

        ListenerUtil.clearDroppedItemsOnDeath(event, player);

        Player killer = AlleyPractice.getInstance().getService(CombatService.class).getLastAttacker(player);

        AlleyPractice.getInstance().getServer().getScheduler().runTaskLater(AlleyPractice.getInstance(), () -> player.spigot().respawn(), 1L);
        Bukkit.getScheduler().runTaskLater(AlleyPractice.getInstance(), () -> profile.getFfaMatch().handleDeath(player, killer), 1L);
    }

    @EventHandler
    private void onPearlLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof EnderPearl)) return;
        if (!(event.getEntity().getShooter() instanceof Player)) return;

        ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);
        LocaleService localeService = AlleyPractice.getInstance().getService(LocaleService.class);

        Player player = (Player) event.getEntity().getShooter();
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() != ProfileState.FFA) return;

        if (AlleyPractice.getInstance().getService(FFASpawnService.class).getCuboid().isIn(player)) {
            event.setCancelled(true);
            InventoryUtil.giveItem(player, Material.ENDER_PEARL, 1);
            player.updateInventory();
            player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.COOLDOWN_CANNOT_USE_AT_FFA_SPAWN));
            return;
        }

        CooldownService cooldownService = AlleyPractice.getInstance().getService(CooldownService.class);
        Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownService.getCooldown(player.getUniqueId(), CooldownType.ENDER_PEARL));

        if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
            event.setCancelled(true);
            InventoryUtil.giveItem(player, Material.ENDER_PEARL, 1);
            player.updateInventory();
            player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.COOLDOWN_PEARL_MUST_WAIT).replace("{time}", String.valueOf(optionalCooldown.get().remainingTime())));
            return;
        }

        Cooldown cooldown = optionalCooldown.orElseGet(() -> {
            Cooldown newCooldown = new Cooldown(CooldownType.ENDER_PEARL, () -> player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.COOLDOWN_CAN_NOW_USE_PEARLS_AGAIN)));
            cooldownService.addCooldown(player.getUniqueId(), CooldownType.ENDER_PEARL, newCooldown);
            return newCooldown;
        });

        cooldown.resetCooldown();
    }

    @EventHandler
    private void onHunger(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);
            Profile profile = profileService.getProfile(player.getUniqueId());
            if (profile.getState() != ProfileState.FFA) return;

            if (profile.getFfaMatch().getKit().isSettingEnabled(KitSettingNoHungerImpl.class)) {
                event.setCancelled(true);
            }
        }
    }
}
