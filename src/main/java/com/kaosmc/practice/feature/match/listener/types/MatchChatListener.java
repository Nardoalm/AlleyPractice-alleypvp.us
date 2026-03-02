package com.kaosmc.practice.feature.match.listener.types;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.feature.match.Match;
import com.kaosmc.practice.feature.match.MatchService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * @author Emmy
 * @project Kaos
 * @since 26/06/2025
 */
public class MatchChatListener implements Listener {

    @EventHandler
    private void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

//        if (player.hasPermission(KaosPractice.getInstance().getService(PluginConstant.class).getAdminPermissionPrefix() + ".bypass.command.restriction")) {
//            return;
//        }

        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        MatchService matchService = KaosPractice.getInstance().getService(MatchService.class);
        LocaleService localeService = KaosPractice.getInstance().getService(LocaleService.class);

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