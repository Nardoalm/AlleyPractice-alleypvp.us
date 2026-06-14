package us.alleypvp.practice.core.profile.command.player.setting.toggle;

import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.feature.music.MusicService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project kaos-practice
 * @since 19/07/2025
 */
public class ToggleLobbyMusicCommand extends BaseCommand {
    @CommandData(
            name = "togglelobbymusic",
            cooldown = 1,
            usage = "togglelobbymusic",
            description = "Ativa ou desativa a música do lobby"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = this.getProfile(player.getUniqueId());
        if (profile.isBusy()) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_MUST_BE_IN_LOBBY));
            return;
        }

        profile.getProfileData().getSettingData().setLobbyMusicEnabled(!profile.getProfileData().getSettingData().isLobbyMusicEnabled());

        MusicService musicService = this.plugin.getService(MusicService.class);
        boolean isEnabled = profile.getProfileData().getSettingData().isLobbyMusicEnabled();
        if (isEnabled) {
            musicService.startMusic(player);
        } else {
            musicService.stopMusic(player);
        }

        player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.PROFILE_TOGGLED_LOBBY_MUSIC)
                .replace("{status}", isEnabled ? "&aenable" : "&cdisable"))
        );
    }
}
