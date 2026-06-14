package us.alleypvp.practice.feature.queue.command.player;

import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.enums.ProfileState;
import us.alleypvp.practice.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
public class LeaveQueueCommand extends BaseCommand {
    @CommandData(
            name = "leavequeue",
            usage = "leavequeue",
            description = "Sai da fila atual."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (!profile.getState().equals(ProfileState.WAITING)) {
            player.sendMessage(CC.translate("&cVocê não está em nenhuma fila."));
            return;
        }

        if (profile.getParty() != null && !profile.getParty().isLeader(player)) {
            player.sendMessage(CC.translate("&cVocê precisa ser o líder da party para sair da fila."));
            return;
        }

        if (profile.getQueueProfile() == null || profile.getQueueProfile().getQueue() == null) {
            player.sendMessage(CC.translate("&cVocê não está em nenhuma fila."));
            return;
        }

        profile.getQueueProfile().getQueue().removePlayer(profile.getQueueProfile());
    }
}
