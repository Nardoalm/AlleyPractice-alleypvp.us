package us.alleypvp.practice.feature.match.listener.types;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.core.locale.LocaleService;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.feature.match.Match;
import us.alleypvp.practice.feature.match.MatchService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * @author Emmy
 * @project Alley
 * @since 26/06/2025
 */
public class MatchChatListener implements Listener {

    @EventHandler
    private void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

//        if (player.hasPermission(AlleyPractice.getInstance().getService(PluginConstant.class).getAdminPermissionPrefix() + ".bypass.command.restriction")) {
//            return;
//        }

        ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);
        MatchService matchService = AlleyPractice.getInstance().getService(MatchService.class);
        LocaleService localeService = AlleyPractice.getInstance().getService(LocaleService.class);

        Profile profile = profileService.getProfile(player.getUniqueId());
        Match match = profile.getMatch();
        if (match == null) return;

        String commandInput = event.getMessage().toLowerCase();

        for (String blockedCommand : matchService.getBlockedCommands()) {
            if (commandInput.startsWith("/" + blockedCommand.toLowerCase())) {
                player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.MATCH_COMMAND_BLOCKED));
                event.setCancelled(true);
                return;
            }
        }
    }
}