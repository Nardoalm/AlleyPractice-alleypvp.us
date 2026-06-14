package us.alleypvp.practice.feature.leaderboard.command;

import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import us.alleypvp.practice.feature.leaderboard.menu.LeaderboardMenu;
import org.bukkit.entity.Player;

public class LeaderboardCommand extends BaseCommand {
    @CommandData(
            name = "leaderboard",
            aliases = {"leaderboards", "lb"},
            usage = "leaderboard",
            description = "View global leaderboards."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new LeaderboardMenu().openMenu(player);
    }
}