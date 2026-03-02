package com.kaosmc.practice.core.profile.command.player;

import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.menu.music.MusicDiscSelectorMenu;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 19/07/2025
 */
public class LobbyMusicCommand extends BaseCommand {
    @CommandData(
            name = "lobbymusic",
            aliases = {"music"},
            usage = "lobbymusic",
            description = "Open the lobby music selector menu"
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