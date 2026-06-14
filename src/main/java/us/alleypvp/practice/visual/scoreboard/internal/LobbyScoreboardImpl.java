package us.alleypvp.practice.visual.scoreboard.internal;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.adapter.core.CoreAdapter;
import us.alleypvp.practice.common.PlayerDisplayUtil;
import us.alleypvp.practice.core.config.ConfigService;
import us.alleypvp.practice.common.text.LevelBadgeUtil;
import us.alleypvp.practice.feature.music.MusicService;
import us.alleypvp.practice.feature.music.MusicSession;
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
import java.util.Optional;

/**
 * @author Emmy
 * @project Alley
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

        ConfigService configService = AlleyPractice.getInstance().getService(ConfigService.class);
        ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);
        MusicService musicService = AlleyPractice.getInstance().getService(MusicService.class);
        CoreAdapter coreAdapter = AlleyPractice.getInstance().getService(CoreAdapter.class);
        if (configService == null || configService.getScoreboardConfig() == null || profileService == null || musicService == null) {
            return Collections.emptyList();
        }

        List<String> scoreboardLines = new ArrayList<>();
        List<String> template = (profile.getParty() != null)
                ? configService.getScoreboardConfig().getStringList("scoreboard.lines.party")
                : configService.getScoreboardConfig().getStringList("scoreboard.lines.lobby");

        Optional<MusicSession> musicStateOptional = musicService.getMusicState(profile.getUuid());

        int experience = profile.getProfileData().getExperience();
        String levelProgressBar = LevelBadgeUtil.getProgressBar(experience, 12);
        String levelProgressDetails = LevelBadgeUtil.getProgressDetails(experience);

        String levelName = LevelBadgeUtil.getDisplayBadge(player, experience);

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
                    Player leaderPlayer = Bukkit.getPlayer(leaderProfile.getUuid());
                    String leaderColor = leaderProfile.getNameColor() != null ? leaderProfile.getNameColor().toString() : "";
                    leaderName = leaderColor + PlayerDisplayUtil.resolveCurrentNick(leaderPlayer, leaderProfile.getName());
                }

                processedLine = CC.translate(processedLine)
                        .replace("{party-size}", String.valueOf(profile.getParty().getMembers().size()))
                        .replace("{party-privacy}", profile.getParty().isPrivate() ? "Privada" : "Pública")
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
