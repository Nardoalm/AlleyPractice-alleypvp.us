package com.kaosmc.practice.visual.scoreboard.internal;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.adapter.core.CoreAdapter;
import com.kaosmc.practice.core.config.ConfigService;
import com.kaosmc.practice.feature.level.LevelService;
import com.kaosmc.practice.feature.level.data.LevelData;
import com.kaosmc.practice.feature.music.MusicService;
import com.kaosmc.practice.feature.music.MusicSession;
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
import java.util.Optional;

/**
 * @author Emmy
 * @project Kaos
 * @since 30/04/2025
 */
public class LobbyScoreboardImpl implements Scoreboard {

    @Override
    public List<String> getLines(Profile profile) {
        ConfigService configService = KaosPractice.getInstance().getService(ConfigService.class);
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        LevelService levelService = KaosPractice.getInstance().getService(LevelService.class);
        MusicService musicService = KaosPractice.getInstance().getService(MusicService.class);
        CoreAdapter coreAdapter = KaosPractice.getInstance().getService(CoreAdapter.class);

        List<String> scoreboardLines = new ArrayList<>();
        List<String> template = (profile.getParty() != null)
                ? configService.getScoreboardConfig().getStringList("scoreboard.lines.party")
                : configService.getScoreboardConfig().getStringList("scoreboard.lines.lobby");

        Optional<MusicSession> musicStateOptional = musicService.getMusicState(profile.getUuid());

        int currentElo = profile.getProfileData().getElo();
        LevelData currentLevel = levelService.getLevel(currentElo);

        for (String line : template) {
            if (line.equalsIgnoreCase("{music}")) {
                musicStateOptional.ifPresent(state -> {
                    List<String> musicTemplate = configService.getScoreboardConfig().getStringList("scoreboard.lines.music");

                    int elapsedSeconds = state.getElapsedSeconds();
                    int totalSeconds = state.getDisc().getDuration();

                    String elapsedTime = TimeUtil.formatTimeFromSeconds(elapsedSeconds);
                    String totalTime = TimeUtil.formatTimeFromSeconds(totalSeconds);
                    String duration = elapsedTime + " / " + totalTime;

                    for (String musicLine : musicTemplate) {
                        scoreboardLines.add(CC.translate(musicLine)
                                .replace("{song-name}", state.getDisc().getTitle())
                                .replace("{song-duration}", duration)
                        );
                    }
                });
                continue;
            }

            String processedLine = CC.translate(line)
                    .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                    .replace("{wins}", String.valueOf(profile.getProfileData().getTotalWins()))
                    .replace("{level}", currentLevel.getDisplayName())
                    .replace("{level_progress_bar}", levelService.getProgressBar(currentElo))
                    .replace("{level_progress_details}", levelService.getProgressDetails(currentElo))
                    .replace("{rank}", coreAdapter.getCore().getRankColor(Bukkit.getPlayer(profile.getUuid())) + coreAdapter.getCore().getRankName(Bukkit.getPlayer(profile.getUuid())))
                    .replace("{playing}", String.valueOf(safeCountState(profileService, ProfileState.PLAYING)))
                    .replace("{in-queue}", String.valueOf(safeCountState(profileService, ProfileState.WAITING)));

            if (profile.getParty() != null) {
                processedLine = CC.translate(processedLine)
                        .replace("{party-size}", String.valueOf(profile.getParty().getMembers().size()))
                        .replace("{party-privacy}", profile.getParty().isPrivate() ? "Private" : "Public")
                        .replace("{party-leader}", profileService.getProfile(profile.getParty().getLeader().getUniqueId()).getFancyName());
            }

            scoreboardLines.add(processedLine);
        }

        return scoreboardLines;
    }

    @Override
    public List<String> getLines(Profile profile, Player player) {
        return Collections.emptyList();
    }
}