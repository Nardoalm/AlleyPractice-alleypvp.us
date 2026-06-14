package us.alleypvp.practice.core.profile.command.player.setting;

import us.alleypvp.practice.common.constants.MessageConstant;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 19/05/2024 - 11:27
 */
public class MatchSettingsCommand extends BaseCommand {
    @CommandData(
            name = "matchsettings",
            usage = "matchsettings",
            description = "Abre o menu de configurações de partida."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        //new MatchSettingsMenu().openMenu(player);
        player.sendMessage(MessageConstant.IN_DEVELOPMENT);
    }
}