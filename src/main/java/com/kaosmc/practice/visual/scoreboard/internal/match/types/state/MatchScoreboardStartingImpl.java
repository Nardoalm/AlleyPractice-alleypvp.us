package com.kaosmc.practice.visual.scoreboard.internal.match.types.state;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.core.config.ConfigService;
import com.kaosmc.practice.feature.match.model.internal.MatchGamePlayer;
import com.kaosmc.practice.feature.match.model.GameParticipant;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.visual.scoreboard.internal.match.MatchScoreboard;
import com.kaosmc.practice.common.animation.internal.types.DotAnimation;
import com.kaosmc.practice.common.text.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Kaos
 * @since 30/04/2025
 */
public class MatchScoreboardStartingImpl implements MatchScoreboard {
    private final DotAnimation dotAnimation;

    /**
     * Constructor for the MatchScoreboardStartingImpl class.
     */
    public MatchScoreboardStartingImpl() {
        this.dotAnimation = new DotAnimation();
    }

    @Override
    public List<String> getLines(Profile profile, Player player, GameParticipant<MatchGamePlayer> you, GameParticipant<MatchGamePlayer> opponent) {
        ConfigService configService = KaosPractice.getInstance().getService(ConfigService.class);
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);

        List<String> scoreboardLines = new ArrayList<>();
        List<String> template = configService.getScoreboardConfig().getStringList("scoreboard.lines.starting");

        for (String line : template) {
            scoreboardLines.add(CC.translate(line)
                    .replace("{opponent}", this.getColoredName(profileService.getProfile(opponent.getLeader().getUuid())))
                    .replace("{opponent-ping}", String.valueOf(this.getPing(opponent.getLeader().getTeamPlayer())))
                    .replace("{player-ping}", String.valueOf(this.getPing(player)))
                    .replace("{duration}", profile.getMatch().getDuration())
                    .replace("{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                    .replace("{dot-animation}", this.dotAnimation.getCurrentFrame())
                    .replace("{countdown}", String.valueOf(profile.getMatch().getRunnable().getStage()))
                    .replace("{kit}", profile.getMatch().getKit().getDisplayName()));
        }

        return scoreboardLines;
    }
}
