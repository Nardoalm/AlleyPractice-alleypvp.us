package us.alleypvp.practice.visual.scoreboard.internal;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.common.animation.internal.types.DotAnimation;
import us.alleypvp.practice.common.text.LevelBadgeUtil;
import us.alleypvp.practice.core.config.ConfigService;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.enums.ProfileState;
import us.alleypvp.practice.visual.scoreboard.Scoreboard;
import us.alleypvp.practice.common.time.TimeUtil;
import us.alleypvp.practice.common.text.CC;
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

        ConfigService configService = AlleyPractice.getInstance().getService(ConfigService.class);
        ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);

        if (configService == null || configService.getScoreboardConfig() == null) return Collections.emptyList();

        // Variáveis de Fila Seguras
        String queueType = "Carregando...";
        String queueKit = "Carregando...";
        String pingRangeDisplay = "N/D";
        long elapsedTime = 0L;

        if (profile.getQueueProfile() != null && profile.getQueueProfile().getQueue() != null) {
            queueType = profile.getQueueProfile().getQueue().getQueueType();
            elapsedTime = profile.getQueueProfile().getElapsedTime();
            if (profile.getQueueProfile().getQueue().getKit() != null) {
                queueKit = profile.getQueueProfile().getQueue().getKit().getDisplayName();
            }
        }

        // Variável de Nível Segura (Antiga linha 46)
        String levelDisplay = LevelBadgeUtil.getDisplayBadge(player, profile.getProfileData().getExperience());
        if (profile.getProfileData().getSettingData() != null) {
            pingRangeDisplay = profile.getProfileData().getSettingData().getPingRangeDisplay();
        }

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
                        .replace("{ping-range}", pingRangeDisplay)
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
