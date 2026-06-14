package us.alleypvp.practice.feature.ffa.command.impl.player;

import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.feature.combat.CombatService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class FFALeaveCommand extends BaseCommand {
    @CommandData(
            name = "ffa.leave",
            aliases = "leaveffa",
            usage = "ffa leave",
            description = "Sai da sua partida de FFA atual"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getFfaMatch() == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_NOT_PLAYING_FFA));
            return;
        }

        CombatService combatService = this.plugin.getService(CombatService.class);
        if (combatService.isPlayerInCombat(player.getUniqueId())) {
            profile.getFfaMatch().handleCombatLog(player, combatService.getLastAttacker(player));
        }

        profile.getFfaMatch().leave(player);
    }
}