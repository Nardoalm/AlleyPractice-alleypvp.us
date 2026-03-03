package com.kaosmc.practice.feature.ffa.command.impl.player;

import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.enums.ProfileState;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Kaos
 * @date 5/27/2024
 */
public class FFASpawnCommand extends BaseCommand {
    @CommandData(
            name = "ffa.spawn",
            usage = "ffa spawn",
            description = "Teleporta para a zona segura do FFA."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() != ProfileState.FFA) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_NOT_PLAYING_FFA));
            return;
        }

        profile.getFfaMatch().teleportToSafeZone(player);
    }
}