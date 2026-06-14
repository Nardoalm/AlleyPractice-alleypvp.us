package us.alleypvp.practice.visual.scoreboard.internal.match;

import us.alleypvp.practice.common.PlayerDisplayUtil;
import us.alleypvp.practice.feature.match.model.internal.MatchGamePlayer;
import us.alleypvp.practice.feature.match.model.GameParticipant;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.common.reflect.utility.ReflectionUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public interface MatchScoreboard {
    /**
     * Gets the scoreboard lines for the given profile in a regular solo match.
     *
     * @param profile  The profile to get the scoreboard lines for.
     * @param player   The player whose scoreboard is being displayed.
     * @param you      The player whose scoreboard is being displayed.
     * @param opponent The opponent player.
     * @return The scoreboard lines.
     */
    List<String> getLines(Profile profile, Player player, GameParticipant<MatchGamePlayer> you, GameParticipant<MatchGamePlayer> opponent);

    /**
     * Gets the corresponding color of the player including the player's name.
     *
     * @param profile The profile of the player.
     * @return The formatted player name with color.
     */
    default String getColoredName(Profile profile) {
        if (profile == null) {
            return ChatColor.WHITE + "Unknown";
        }

        ChatColor nameColor = profile.getNameColor();
        Player onlineTarget = Bukkit.getPlayer(profile.getUuid());
        String name = PlayerDisplayUtil.resolveCurrentNick(onlineTarget, profile.getName());

        if (nameColor != null) {
            return nameColor + name;
        } else {
            return ChatColor.WHITE + name;
        }
    }

    /**
     * Gets the ping of the player by using reflect.
     *
     * @param player The player to get the ping for.
     * @return The ping of the player.
     */
    default int getPing(Player player) {
        if (player == null) {
            return 0;
        }

        return ReflectionUtility.getPing(player);
    }
}
