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

public class TogglePartyMessagesCommand extends BaseCommand {
    @Override
    @CommandData(
            name = "togglepartymessages",
            cooldown = 1,
            usage = "togglepartymessages",
            description = "Ativa ou desativa mensagens da party."
    )
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = this.getProfile(player.getUniqueId());
        profile.getProfileData().getSettingData().setPartyMessagesEnabled(!profile.getProfileData().getSettingData().isPartyMessagesEnabled());

        player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.PROFILE_TOGGLED_PARTY_MESSAGES)
                .replace("{status}", profile.getProfileData().getSettingData().isPartyMessagesEnabled() ? "&aativou" : "&cdesativou"))
        );
    }
}
