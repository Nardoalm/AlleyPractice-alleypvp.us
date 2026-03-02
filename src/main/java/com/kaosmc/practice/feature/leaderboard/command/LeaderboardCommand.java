package com.kaosmc.practice.feature.leaderboard.command;

import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import com.kaosmc.practice.feature.leaderboard.menu.LeaderboardMenu;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @date 19/05/2024 - 11:29
 */
public class LeaderboardCommand extends BaseCommand {
    @CommandData(
            name = "leaderboard",
            aliases = {"leaderboards", "lb"},
            usage = "leaderboard",
            description = "View the global leaderboards."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new LeaderboardMenu().openMenu(player);
    }
}
