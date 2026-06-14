package us.alleypvp.practice.visual.scoreboard.internal;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.common.PlayerDisplayUtil;
import us.alleypvp.practice.core.config.ConfigService;
import us.alleypvp.practice.feature.match.internal.types.DefaultMatch;
import us.alleypvp.practice.feature.match.model.internal.MatchGamePlayer;
import us.alleypvp.practice.feature.match.model.GameParticipant;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.visual.scoreboard.Scoreboard;
import us.alleypvp.practice.common.text.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public class SpectatorScoreboardImpl implements Scoreboard {
    @Override
    public List<String> getLines(Profile profile) {
        if (profile == null || profile.getProfileData() == null) {
            return Collections.emptyList();
        }

        Player player = Bukkit.getPlayer(profile.getUuid());
        if (player == null || !player.isOnline()) {
            return Collections.emptyList();
        }
        ConfigService configService = AlleyPractice.getInstance().getService(ConfigService.class);
        if (configService == null || configService.getScoreboardConfig() == null || profile.getMatch() == null) {
            return Collections.emptyList();
        }

        List<String> scoreboardLines = new ArrayList<>();

        GameParticipant<MatchGamePlayer> participantA = getParticipantSafely(profile.getMatch().getParticipants(), 0);
        GameParticipant<MatchGamePlayer> participantB = getParticipantSafely(profile.getMatch().getParticipants(), 1);

        String playerAName = getPlayerNameSafely(participantA);
        String playerBName = getPlayerNameSafely(participantB);
        String pingA = getPingSafely(participantA);
        String pingB = getPingSafely(participantB);

        if (profile.getMatch() instanceof DefaultMatch) {
            for (String line : configService.getScoreboardConfig().getStringList("scoreboard.lines.spectating.regular-match")) {
                scoreboardLines.add(CC.translate(line)
                        .replace("{playerA}", playerAName)
                        .replace("{playerB}", playerBName)
                        .replace("{pingA}", pingA)
                        .replace("{pingB}", pingB)
                        .replace("{colorA}", String.valueOf(((DefaultMatch) profile.getMatch()).getTeamAColor()))
                        .replace("{colorB}", String.valueOf(((DefaultMatch) profile.getMatch()).getTeamBColor()))
                        .replace("{duration}", profile.getMatch().getDuration())
                        .replace("{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lINDEFINIDO" : profile.getMatch().getArena().getDisplayName())
                        .replace("{kit}", profile.getMatch().getKit().getDisplayName()));
            }
        } else if (profile.getFfaMatch() != null) {
            for (String line : configService.getScoreboardConfig().getStringList("scoreboard.lines.spectating.ffa")) {
                scoreboardLines.add(CC.translate(line)
                        .replace("{arena}", profile.getFfaMatch().getArena().getDisplayName() == null ? "&c&lINDEFINIDO" : profile.getFfaMatch().getArena().getDisplayName())
                        .replace("{kit}", profile.getFfaMatch().getKit().getDisplayName()));
            }
        }

        return scoreboardLines;
    }

    @Override
    public List<String> getLines(Profile profile, Player player) {
        return Collections.emptyList();
    }

    /**
     * Safely gets a participant from the list at the specified index.
     *
     * @param participants The list of participants
     * @param index        The index to retrieve
     * @return The participant at the index, or null if not available
     */
    private GameParticipant<MatchGamePlayer> getParticipantSafely(List<GameParticipant<MatchGamePlayer>> participants, int index) {
        if (participants == null || index >= participants.size() || index < 0) {
            return null;
        }
        return participants.get(index);
    }

    /**
     * Safely gets the player name from a participant.
     *
     * @param participant The participant to get the name from
     * @return The player name, or "Disconnected" if not available
     */
    private String getPlayerNameSafely(GameParticipant<MatchGamePlayer> participant) {
        if (participant == null) {
            return "&c&lDisconnected";
        }

        if (!participant.getPlayers().isEmpty()) {
            MatchGamePlayer gamePlayer = participant.getPlayers().get(0);
            return PlayerDisplayUtil.resolveCurrentNick(gamePlayer.getTeamPlayer(), gamePlayer.getUsername());
        }

        if (!participant.getAllPlayers().isEmpty()) {
            MatchGamePlayer gamePlayer = participant.getAllPlayers().get(0);
            String displayName = PlayerDisplayUtil.resolveCurrentNick(gamePlayer.getTeamPlayer(), gamePlayer.getUsername());
            return "&7" + displayName + " &c(DC)";
        }

        return "&c&lDisconnected";
    }

    /**
     * Safely gets the ping from a participant.
     *
     * @param participant The participant to get the ping from
     * @return The ping as a string, or "0" if not available
     */
    private String getPingSafely(GameParticipant<MatchGamePlayer> participant) {
        if (participant == null) {
            return "0";
        }

        if (!participant.getPlayers().isEmpty()) {
            Player player = participant.getPlayers().get(0).getTeamPlayer();
            if (player != null) {
                return String.valueOf(this.getPing(player));
            }
        }
        return "0";
    }
}
