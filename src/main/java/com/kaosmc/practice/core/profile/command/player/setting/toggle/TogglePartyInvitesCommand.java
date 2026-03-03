package com.kaosmc.practice.core.profile.command.player.setting.toggle;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
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
                .replace("{status}", profile.getProfileData().getSettingData().isPartyInvitesEnabled() ? "&aenabled" : "&cdisabled"))
        );
    }
}
