package com.kaosmc.practice.feature.match.task.other;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.text.LevelBadgeUtil;
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
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        CooldownService cooldownService = KaosPractice.getInstance().getService(CooldownService.class);
        if (profileService == null || cooldownService == null) {
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = profileService.getProfile(player.getUniqueId());
            if (profile == null) {
                continue;
            }

            if (profile.getState() == ProfileState.PLAYING || profile.getState() == ProfileState.FFA) {
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
                } else {
                    updatePlayerLevelProgressBar(player, profile);
                }
            } else {
                updatePlayerLevelProgressBar(player, profile);
            }
        }
    }

    private void updatePlayerLevelProgressBar(Player player, Profile profile) {
        int experience = 0;
        if (profile != null && profile.getProfileData() != null) {
            experience = Math.max(0, profile.getProfileData().getExperience());
        }

        int level = LevelBadgeUtil.getLevel(experience);
        float progressRatio = LevelBadgeUtil.getProgressRatio(experience);

        if (player.getLevel() != level) {
            player.setLevel(level);
        }

        if (Math.abs(player.getExp() - progressRatio) > 0.0001F) {
            player.setExp(progressRatio);
        }
    }
}
