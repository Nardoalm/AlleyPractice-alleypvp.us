package us.alleypvp.practice.core.profile.command.player;

import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.menu.music.MusicDiscSelectorMenu;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project kaos-practice
 * @since 19/07/2025
 */
public class LobbyMusicCommand extends BaseCommand {
    @CommandData(
            name = "lobbymusic",
            aliases = {"music"},
            usage = "lobbymusic",
            description = "Abre o menu seletor de música do lobby"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = this.getProfile(player.getUniqueId());
        if (!profile.isInLobbyOrInQueue()) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_MUST_BE_IN_LOBBY));
            return;
        }

        new MusicDiscSelectorMenu().openMenu(player);
    }
}