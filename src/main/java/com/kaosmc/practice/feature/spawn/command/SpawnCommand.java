package com.kaosmc.practice.feature.spawn.command;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.PlayerUtil;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.enums.ProfileState;
import com.kaosmc.practice.feature.ffa.FFAMatch;
import com.kaosmc.practice.feature.ffa.FFAState;
import com.kaosmc.practice.feature.hotbar.HotbarService;
import com.kaosmc.practice.feature.spawn.SpawnService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @date 29/04/2024 - 19:01
 */
public class SpawnCommand extends BaseCommand {
    @CommandData(
            name = "spawn",
            isAdminOnly = true,
            usage = "spawn",
            description = "Teleport to spawn."
    )
    @Override
    public void onCommand(CommandArgs args) {
        Player player = args.getPlayer();
        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        ProfileState state = profile.getState();

        switch (state) {
            case FFA:
                FFAMatch ffaMatch = profile.getFfaMatch();
                if (ffaMatch == null) return;

                if (ffaMatch.getGameFFAPlayer(player).getState() == FFAState.FIGHTING) {
                    ffaMatch.teleportToSafeZone(player);
                } else {
                    ffaMatch.leave(player);
                }
                break;
            case PLAYING:
                player.sendMessage(KaosPractice.getInstance().getService(LocaleService.class).getString(GlobalMessagesLocaleImpl.ERROR_YOU_MUST_BE_IN_LOBBY));
            case SPECTATING:
                profile.getMatch().removeSpectator(player, false);
                break;
            default:
                this.sendToSpawn(player);
                break;
        }
    }

    /**
     * Sends the player to the spawn location and resets their state.
     *
     * @param player The player to send to spawn.
     */
    private void sendToSpawn(Player player) {
        PlayerUtil.reset(player, false, true);

        this.plugin.getService(SpawnService.class).teleportToSpawn(player);
        this.plugin.getService(HotbarService.class).applyHotbarItems(player);

        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.SPAWN_TELEPORTED));
    }
}