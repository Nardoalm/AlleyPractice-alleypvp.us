package us.alleypvp.practice.feature.queue.command.player;

import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.enums.ProfileState;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import us.alleypvp.practice.feature.queue.menu.QueuesMenuModern;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project kaos-practice
 * @date 22/06/2025
 */
public class UnrankedCommand extends BaseCommand {
    @CommandData(
            name = "unranked",
            usage = "unranked",
            description = "Abre o menu da fila unranked."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = this.plugin.getService(ProfileService.class).getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.LOBBY) {
            return;
        }

        new QueuesMenuModern().openMenu(player);
    }
}