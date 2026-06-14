package us.alleypvp.practice.feature.title.command;

import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import us.alleypvp.practice.feature.title.menu.TitleMenu;
import us.alleypvp.practice.core.profile.ProfileService;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 22/04/2025
 */
public class TitleCommand extends BaseCommand {
    @CommandData(
            name = "title",
            aliases = {"titles"},
            usage = "title",
            description = "Abre o menu de títulos."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new TitleMenu(this.plugin.getService(ProfileService.class).getProfile(player.getUniqueId())).openMenu(player);
    }
}