package com.kaosmc.practice.visual.scoreboard.internal;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.core.config.ConfigService;
import com.kaosmc.practice.feature.level.LevelService;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.enums.ProfileState;
import com.kaosmc.practice.visual.scoreboard.Scoreboard;
import com.kaosmc.practice.common.time.TimeUtil;
import com.kaosmc.practice.common.text.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Kaos
 * @since 30/04/2025
 */
public class QueueScoreboardImpl implements Scoreboard {
    @Override
    public List<String> getLines(Profile profile) {
        if (profile == null || profile.getProfileData() == null) {
            return Collections.emptyList();
        }
        Player player = Bukkit.getPlayer(profile.getUuid());
        if (player == null || !player.isOnline()) {
            return Collections.emptyList();
        }
        ConfigService configService = KaosPractice.getInstance().getService(ConfigService.class);
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        LevelService levelService = KaosPractice.getInstance().getService(LevelService.class);

        List<String> scoreboardLines = new ArrayList<>();
        for (String line : configService.getScoreboardConfig().getStringList("scoreboard.lines.waiting")) {
            scoreboardLines.add(CC.translate(line)
                    .replaceAll("\\{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                    .replaceAll("\\{playing}", String.valueOf(safeCountState(profileService, ProfileState.PLAYING)))
                    .replaceAll("\\{in-queue}", String.valueOf(safeCountState(profileService, ProfileState.WAITING)))
                    .replaceAll("\\{wins}", String.valueOf(profile.getProfileData().getTotalWins()))
                    .replaceAll("\\{queued-type}", profile.getQueueProfile().getQueue().getQueueType())
                    .replaceAll("\\{level}", String.valueOf(levelService.getLevel(profile.getProfileData().getGlobalLevel()).getDisplayName()))
                    .replaceAll("\\{queued-time}", TimeUtil.getFormattedElapsedTime(profile.getQueueProfile().getElapsedTime()))
                    .replaceAll("\\{dot-animation}", this.getDotAnimation().getCurrentFrame())
                    .replaceAll("\\{queued-kit}", profile.getQueueProfile().getQueue().getKit().getDisplayName())
            );
        }

        return scoreboardLines;
    }

    @Override
    public List<String> getLines(Profile profile, Player player) {
        return Collections.emptyList();
    }
}