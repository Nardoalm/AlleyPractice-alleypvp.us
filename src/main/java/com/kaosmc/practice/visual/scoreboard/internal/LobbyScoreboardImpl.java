package com.kaosmc.practice.visual.scoreboard.internal;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.adapter.core.CoreAdapter;
import com.kaosmc.practice.core.config.ConfigService;
import com.kaosmc.practice.common.text.LevelBadgeUtil;
import com.kaosmc.practice.feature.level.LevelService;
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
        // 1. Prevenção: O perfil ou os dados ainda não carregaram
        if (profile == null || profile.getProfileData() == null) {
            return Collections.singletonList("&cCarregando dados...");
        }

        // 2. Prevenção: O jogador não está online no momento do tick
        Player player = Bukkit.getPlayer(profile.getUuid());
        if (player == null || !player.isOnline()) {
            return Collections.emptyList();
        }

        ConfigService configService = KaosPractice.getInstance().getService(ConfigService.class);
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        LevelService levelService = KaosPractice.getInstance().getService(LevelService.class);
        MusicService musicService = KaosPractice.getInstance().getService(MusicService.class);
        CoreAdapter coreAdapter = KaosPractice.getInstance().getService(CoreAdapter.class);
        if (configService == null || configService.getScoreboardConfig() == null || profileService == null || musicService == null) {
            return Collections.emptyList();
        }

        List<String> scoreboardLines = new ArrayList<>();
        List<String> template = (profile.getParty() != null)
                ? configService.getScoreboardConfig().getStringList("scoreboard.lines.party")
                : configService.getScoreboardConfig().getStringList("scoreboard.lines.lobby");

        Optional<MusicSession> musicStateOptional = musicService.getMusicState(profile.getUuid());

        int currentElo = profile.getProfileData().getElo();
        String levelProgressBar = levelService != null ? levelService.getProgressBar(currentElo) : "";
        String levelProgressDetails = levelService != null ? levelService.getProgressDetails(currentElo) : "";

        String levelName = LevelBadgeUtil.getBadge(profile.getProfileData().getExperience());

        // 4. Prevenção: CoreAdapter ou Core ausente
        String rankStr = "";
        if (coreAdapter != null && coreAdapter.getCore() != null) {
            rankStr = coreAdapter.getCore().getRankColor(player) + coreAdapter.getCore().getRankName(player);
        }

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
                    .replace("{level}", levelName) // Usando a variável segura
                    .replace("{nivel}", levelName)
                    .replace("{nível}", levelName)
                    .replace("{level_progress_bar}", levelProgressBar)
                    .replace("{level_progress_details}", levelProgressDetails)
                    .replace("{rank}", rankStr) // Usando a variável segura
                    .replace("{playing}", String.valueOf(safeCountState(profileService, ProfileState.PLAYING)))
                    .replace("{in-queue}", String.valueOf(safeCountState(profileService, ProfileState.WAITING)));

            if (profile.getParty() != null) {
                // 5. Prevenção: O líder da party desconectou e o perfil não está no cache
                String leaderName = "Desconhecido";
                Profile leaderProfile = profileService.getProfile(profile.getParty().getLeader().getUniqueId());
                if (leaderProfile != null) {
                    leaderName = leaderProfile.getFancyName();
                }

                processedLine = CC.translate(processedLine)
                        .replace("{party-size}", String.valueOf(profile.getParty().getMembers().size()))
                        .replace("{party-privacy}", profile.getParty().isPrivate() ? "Private" : "Public")
                        .replace("{party-leader}", leaderName); // Usando variável segura
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
