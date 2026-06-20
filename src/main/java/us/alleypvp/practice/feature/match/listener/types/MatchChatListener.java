package us.alleypvp.practice.feature.match.listener.types;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.core.config.ConfigService;
import us.alleypvp.practice.core.locale.LocaleService;
import us.alleypvp.practice.core.locale.internal.impl.SettingsLocaleImpl;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.feature.match.Match;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class MatchChatListener implements Listener {

    @EventHandler
    private void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("kaos.match.commands.bypass")) {
            return;
        }

        ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);
        LocaleService localeService = AlleyPractice.getInstance().getService(LocaleService.class);
        ConfigService configService = AlleyPractice.getInstance().getService(ConfigService.class);

        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile == null) {
            return;
        }

        Match match = profile.getMatch();
        if (match == null) {
            return;
        }

        String commandInput = event.getMessage().toLowerCase();
        String command = commandInput.split(" ")[0].replace("/", "");

        List<String> blockedCommands = configService.getSettingsConfig().getStringList("game.blocked-commands");

        if (blockedCommands != null && blockedCommands.contains(command)) {
            player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.MATCH_COMMAND_BLOCKED));
            event.setCancelled(true);
        }
    }
}