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
 * @project kaos-practice
 * @since 13/07/2025
 */
public class ToggleDuelRequestsCommand extends BaseCommand {
    @CommandData(
            name = "toggleduelrequests",
            cooldown = 1,
            usage = "toggleduelrequests",
            description = "Ativa ou desativa o recebimento de pedidos de duelo."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = this.getProfile(player.getUniqueId());
        profile.getProfileData().getSettingData().setReceiveDuelRequestsEnabled(!profile.getProfileData().getSettingData().isReceiveDuelRequestsEnabled());

        player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.PROFILE_TOGGLED_DUEL_REQUESTS)
                .replace("{status}", profile.getProfileData().getSettingData().isReceiveDuelRequestsEnabled() ? "&aativou" : "&cdesativou"))
        );
    }
}
