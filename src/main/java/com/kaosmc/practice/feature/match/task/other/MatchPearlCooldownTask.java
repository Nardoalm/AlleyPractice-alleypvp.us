package com.kaosmc.practice.feature.match.task.other;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.feature.cooldown.Cooldown;
import com.kaosmc.practice.feature.cooldown.CooldownService;
import com.kaosmc.practice.feature.cooldown.CooldownType;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.enums.ProfileState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

/**
 * @author Remi
 * @project kaos-practice
 * @date 26/06/2025
 */
public class MatchPearlCooldownTask extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = KaosPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());

            if (profile.getState() == ProfileState.PLAYING || profile.getState() == ProfileState.FFA) {
                CooldownService cooldownService = KaosPractice.getInstance().getService(CooldownService.class);
                Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownService.getCooldown(player.getUniqueId(), CooldownType.ENDER_PEARL));

                if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {

                    Cooldown cooldown = optionalCooldown.get();

                    long remainingMillis = cooldown.remainingTimeMillis();
                    long totalDuration = cooldown.getType().getCooldownDuration();
                    int remainingTime = optionalCooldown.get().remainingTime();

                    player.setLevel(remainingTime);

                    if (totalDuration > 0) {
                        player.setExp((float) remainingMillis / totalDuration);
                    }
                }
            } else {
                if (player.getLevel() > 0) {
                    player.setLevel(0);
                }

                if (player.getExp() > 0.0F) {
                    player.setExp(0.0F);
                }
            }
        }
    }
}
