package us.alleypvp.practice.feature.spawn.command;

import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.feature.hotbar.HotbarService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 29/04/2024 - 18:45
 */
public class SpawnItemsCommand extends BaseCommand {
    @CommandData(
            name = "spawnitems",
            aliases = {"lobbyitems"},
            isAdminOnly = true,
            usage = "spawnitems",
            description = "Dá ao jogador os itens de spawn."
    )
    @Override
    public void onCommand(CommandArgs args) {
        Player player = args.getPlayer();

        this.plugin.getService(HotbarService.class).applyHotbarItems(player);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.SPAWN_ITEMS_GIVEN));
    }
}