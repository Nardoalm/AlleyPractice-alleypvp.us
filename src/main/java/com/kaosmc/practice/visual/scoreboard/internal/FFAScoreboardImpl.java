package com.kaosmc.practice.visual.scoreboard.internal;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.feature.combat.CombatService;
import com.kaosmc.practice.core.config.ConfigService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.data.types.ProfileFFAData;
import com.kaosmc.practice.feature.ffa.spawn.FFASpawnService;
import com.kaosmc.practice.visual.scoreboard.Scoreboard;
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
        ProfileFFAData profileFFAData = profile.getProfileData().getFfaData().get(profile.getFfaMatch().getKit().getName());

        FFASpawnService ffaSpawnService = KaosPractice.getInstance().getService(FFASpawnService.class);
        ConfigService configService = KaosPractice.getInstance().getService(ConfigService.class);
        CombatService combatService = KaosPractice.getInstance().getService(CombatService.class);

        List<String> scoreboardLines = new ArrayList<>();

        List<String> ffaLines = configService.getScoreboardConfig().getStringList("scoreboard.lines.ffa");
        List<String> combatTagLines = configService.getScoreboardConfig().getStringList("ffa-combat-tag");

        for (String line : ffaLines) {
            if (line.contains("{player-combat}")) {
                if (combatService.isPlayerInCombat(player.getUniqueId())) {
                    for (String combatLine : combatTagLines) {
                        scoreboardLines.add(CC.translate(combatLine
                                .replaceAll("\\{combat-tag}", combatService.getRemainingTimeFormatted(player))));
                    }
                }
            } else {
                scoreboardLines.add(CC.translate(line)
                        .replaceAll("\\{kit}", profile.getFfaMatch().getKit().getDisplayName())
                        .replaceAll("\\{players}", String.valueOf(profile.getFfaMatch().getPlayers().size()))
                        .replaceAll("\\{zone}", ffaSpawnService.getCuboid().isIn(player) ? "Spawn" : "Warzone")
                        .replaceAll("\\{ks}", String.valueOf(profileFFAData.getKillstreak()))
                        .replaceAll("\\{kills}", String.valueOf(profileFFAData.getKills()))
                        .replaceAll("\\{deaths}", String.valueOf(profileFFAData.getDeaths()))
                        .replaceAll("\\{ping}", String.valueOf(this.getPing(player))));
            }
        }

        return scoreboardLines;
    }
}