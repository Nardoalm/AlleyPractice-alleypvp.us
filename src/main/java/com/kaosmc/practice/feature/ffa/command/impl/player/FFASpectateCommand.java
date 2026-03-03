package com.kaosmc.practice.feature.ffa.command.impl.player;

import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.enums.ProfileState;
import com.kaosmc.practice.feature.ffa.FFAMatch;
import com.kaosmc.practice.feature.ffa.FFAService;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @since 04/06/2025
 */
public class FFASpectateCommand extends BaseCommand {
    @CommandData(
            name = "ffa.spectate",
            usage = "spectate <ffaKit>",
            aliases = {"specffa", "spectateffa"},
            description = "Assiste uma partida de FFA"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.LOBBY) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_MUST_BE_IN_LOBBY));
            return;
        }

        String ffaKitName = args[0];
        FFAService ffaService = this.plugin.getService(FFAService.class);
        Kit ffaKit = ffaService.getFfaKits().stream()
                .filter(kit -> kit.getName().equalsIgnoreCase(ffaKitName))
                .findFirst()
                .orElse(null);

        if (ffaKit == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.KIT_NOT_FOUND).replace("{kit-name}", ffaKitName));
            return;
        }

        if (!ffaKit.isFfaEnabled()) {
            //should never happen, but just in case
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.FFA_DISABLED));
            return;
        }

        FFAMatch match = ffaService.getMatches().stream()
                .filter(m -> m.getKit().equals(ffaKit))
                .findFirst()
                .orElse(null);

        if (match == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.FFA_NOT_FOUND).replace("{ffa-name}", ffaKitName));
            return;
        }

        if (match.getSpectators().contains(player.getUniqueId())) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_ALREADY_SPECTATING_FFA));
            return;
        }

        match.addSpectator(player);
    }
}