package us.alleypvp.practice.core.profile.command.player.setting.toggle;

import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 23:35
 */

public class TogglePartyInvitesCommand extends BaseCommand {
    @Override
    @CommandData(
            name = "togglepartyinvites",
            cooldown = 1,
            usage = "togglepartyinvites",
            description = "Ativa ou desativa convites de party."
    )
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = this.getProfile(player.getUniqueId());
        profile.getProfileData().getSettingData().setPartyInvitesEnabled(!profile.getProfileData().getSettingData().isPartyInvitesEnabled());

        player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.PROFILE_TOGGLED_PARTY_INVITES)
                .replace("{status}", profile.getProfileData().getSettingData().isPartyInvitesEnabled() ? "&aenable" : "&cdisable"))
        );
    }
}
