package us.alleypvp.practice.visual.scoreboard.internal;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.feature.combat.CombatService;
import us.alleypvp.practice.core.config.ConfigService;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.data.types.ProfileFFAData;
import us.alleypvp.practice.feature.ffa.spawn.FFASpawnService;
import us.alleypvp.practice.visual.scoreboard.Scoreboard;
import us.alleypvp.practice.common.text.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public class FFAScoreboardImpl implements Scoreboard {
    @Override
    public List<String> getLines(Profile profile) {
        return Collections.emptyList();
    }

    /**
     * Get the title of the scoreboard.
     *
     * @param profile The profile to get the title for.
     * @param player  The player to get the title for.
     * @return The title of the scoreboard.
     */
    @Override
    public List<String> getLines(Profile profile, Player player) {
        if (profile == null || profile.getProfileData() == null) {
            return Collections.emptyList();
        }

        if (player == null || !player.isOnline()) {
            return Collections.emptyList();
        }
        if (profile.getFfaMatch() == null || profile.getFfaMatch().getKit() == null) {
            return Collections.emptyList();
        }
        ProfileFFAData profileFFAData = profile.getProfileData().getFfaData().get(profile.getFfaMatch().getKit().getName());

        FFASpawnService ffaSpawnService = AlleyPractice.getInstance().getService(FFASpawnService.class);
        ConfigService configService = AlleyPractice.getInstance().getService(ConfigService.class);
        CombatService combatService = AlleyPractice.getInstance().getService(CombatService.class);
        if (profileFFAData == null || ffaSpawnService == null || ffaSpawnService.getCuboid() == null
                || configService == null || configService.getScoreboardConfig() == null || combatService == null) {
            return Collections.emptyList();
        }

        List<String> scoreboardLines = new ArrayList<>();

        List<String> ffaLines = configService.getScoreboardConfig().getStringList("scoreboard.lines.ffa");
        List<String> combatTagLines = configService.getScoreboardConfig().getStringList("ffa-combat-tag");

        for (String line : ffaLines) {
            if (line.contains("{player-combat}")) {
                if (combatService.isPlayerInCombat(player.getUniqueId())) {
                    for (String combatLine : combatTagLines) {
                        scoreboardLines.add(CC.translate(combatLine
                                .replace("{combat-tag}", combatService.getRemainingTimeFormatted(player))));
                    }
                }
            } else {
                scoreboardLines.add(CC.translate(line)
                        .replace("{kit}", profile.getFfaMatch().getKit().getDisplayName())
                        .replace("{players}", String.valueOf(profile.getFfaMatch().getPlayers().size()))
                        .replace("{zone}", ffaSpawnService.getCuboid().isIn(player) ? "Spawn" : "Warzone")
                        .replace("{ks}", String.valueOf(profileFFAData.getKillstreak()))
                        .replace("{kills}", String.valueOf(profileFFAData.getKills()))
                        .replace("{deaths}", String.valueOf(profileFFAData.getDeaths()))
                        .replace("{ping}", String.valueOf(this.getPing(player))));
            }
        }

        return scoreboardLines;
    }
}
