package us.alleypvp.practice.core.profile.command.player;

import us.alleypvp.practice.feature.level.menu.LevelMenu;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 22/04/2025
 */
public class LevelCommand extends BaseCommand {
    @CommandData(
            name = "level",
            aliases = {"levels"},
            usage = "level",
            description = "Veja informações sobre seu nível atual."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new LevelMenu(this.getProfile(player.getUniqueId())).openMenu(player);
    }
}