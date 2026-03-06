package com.kaosmc.practice.visual.scoreboard.internal;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.animation.internal.types.DotAnimation;
import com.kaosmc.practice.common.text.LevelBadgeUtil;
import com.kaosmc.practice.core.config.ConfigService;
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

public class QueueScoreboardImpl implements Scoreboard {
    private final DotAnimation dotAnimation = new DotAnimation();

    @Override
    public List<String> getLines(Profile profile) {
        if (profile == null || profile.getProfileData() == null) return Collections.emptyList();

        Player player = Bukkit.getPlayer(profile.getUuid());
        if (player == null || !player.isOnline()) return Collections.emptyList();

        ConfigService configService = KaosPractice.getInstance().getService(ConfigService.class);
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);

        if (configService == null || configService.getScoreboardConfig() == null) return Collections.emptyList();

        // Variáveis de Fila Seguras
        String queueType = "Carregando...";
        String queueKit = "Carregando...";
        long elapsedTime = 0L;

        if (profile.getQueueProfile() != null && profile.getQueueProfile().getQueue() != null) {
            queueType = profile.getQueueProfile().getQueue().getQueueType();
            elapsedTime = profile.getQueueProfile().getElapsedTime();
            if (profile.getQueueProfile().getQueue().getKit() != null) {
                queueKit = profile.getQueueProfile().getQueue().getKit().getDisplayName();
            }
        }

        // Variável de Nível Segura (Antiga linha 46)
        String levelDisplay = LevelBadgeUtil.getBadge(profile.getProfileData().getExperience());

        List<String> scoreboardLines = new ArrayList<>();
        List<String> configLines = configService.getScoreboardConfig().getStringList("scoreboard.lines.waiting");

        if (configLines != null) {
            for (String line : configLines) {
                scoreboardLines.add(CC.translate(line)
                        .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                        .replace("{playing}", String.valueOf(safeCountState(profileService, ProfileState.PLAYING)))
                        .replace("{in-queue}", String.valueOf(safeCountState(profileService, ProfileState.WAITING)))
                        .replace("{wins}", String.valueOf(profile.getProfileData().getTotalWins()))
                        .replace("{queued-type}", queueType != null ? queueType : "Normal")
                        .replace("{level}", levelDisplay)
                        .replace("{nivel}", levelDisplay)
                        .replace("{nível}", levelDisplay)
                        .replace("{dot-animation}", this.dotAnimation.getCurrentFrame())
                        .replace("{queued-time}", TimeUtil.getFormattedElapsedTime(elapsedTime))
                        .replace("{queued-kit}", queueKit != null ? queueKit : "Nenhum")
                );
            }
        }
        return scoreboardLines;
    }

    public int safeCountState(ProfileService service, ProfileState state) {
        if (service == null) return 0;
        return (int) Bukkit.getOnlinePlayers().stream().filter(p -> {
            Profile prof = service.getProfile(p.getUniqueId());
            return prof != null && prof.getState() == state;
        }).count();
    }

    @Override
    public List<String> getLines(Profile profile, Player player) { return Collections.emptyList(); }
}
