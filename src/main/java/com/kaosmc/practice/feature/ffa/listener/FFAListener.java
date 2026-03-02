package com.kaosmc.practice.feature.ffa.listener;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.InventoryUtil;
import com.kaosmc.practice.common.ListenerUtil;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.message.GameMessagesLocaleImpl;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.enums.ProfileState;
import com.kaosmc.practice.feature.combat.CombatService;
import com.kaosmc.practice.feature.cooldown.Cooldown;
import com.kaosmc.practice.feature.cooldown.CooldownService;
import com.kaosmc.practice.feature.cooldown.CooldownType;
import com.kaosmc.practice.feature.ffa.spawn.FFASpawnService;
import com.kaosmc.practice.feature.kit.setting.types.mechanic.KitSettingNoHungerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * @author Emmy
 * @project Kaos
 * @date 25/05/2024 - 14:24
 */
public class FFAListener implements Listener {

    @EventHandler
    private void onPlayerDropItem(PlayerDropItemEvent event) {
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        LocaleService localeService = KaosPractice.getInstance().getService(LocaleService.class);

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
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.FFA) {
            return;
        }

        ItemStack item = event.getItem();
        if (item.getType() == Material.POTION) {
            KaosPractice.getInstance().getServer().getScheduler().runTaskLater(KaosPractice.getInstance(), () -> {
                player.getInventory().removeItem(new ItemStack(Material.GLASS_BOTTLE, 1));
                player.updateInventory();
            }, 1L);
        }
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.FFA) return;
        event.setDeathMessage(null);

        ListenerUtil.clearDroppedItemsOnDeath(event, player);

        Player killer = KaosPractice.getInstance().getService(CombatService.class).getLastAttacker(player);

        KaosPractice.getInstance().getServer().getScheduler().runTaskLater(KaosPractice.getInstance(), () -> player.spigot().respawn(), 1L);
        Bukkit.getScheduler().runTaskLater(KaosPractice.getInstance(), () -> profile.getFfaMatch().handleDeath(player, killer), 1L);
    }

    @EventHandler
    private void onPearlLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof EnderPearl)) return;
        if (!(event.getEntity().getShooter() instanceof Player)) return;

        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        LocaleService localeService = KaosPractice.getInstance().getService(LocaleService.class);

        Player player = (Player) event.getEntity().getShooter();
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() != ProfileState.FFA) return;

        if (KaosPractice.getInstance().getService(FFASpawnService.class).getCuboid().isIn(player)) {
            event.setCancelled(true);
            InventoryUtil.giveItem(player, Material.ENDER_PEARL, 1);
            player.updateInventory();
            player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.COOLDOWN_CANNOT_USE_AT_FFA_SPAWN));
            return;
        }

        CooldownService cooldownService = KaosPractice.getInstance().getService(CooldownService.class);
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
            ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
            Profile profile = profileService.getProfile(player.getUniqueId());
            if (profile.getState() != ProfileState.FFA) return;

            if (profile.getFfaMatch().getKit().isSettingEnabled(KitSettingNoHungerImpl.class)) {
                event.setCancelled(true);
            }
        }
    }
}