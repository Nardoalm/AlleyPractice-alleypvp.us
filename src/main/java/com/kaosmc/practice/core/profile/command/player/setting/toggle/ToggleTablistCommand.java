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

public class ToggleTablistCommand extends BaseCommand {
    @Override
    @CommandData(
            name = "toggletablist",
            cooldown = 1,
            usage = "toggletablist",
            description = "Ativa ou desativa a tablist."
    )
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = this.getProfile(player.getUniqueId());
        profile.getProfileData().getSettingData().setTablistEnabled(!profile.getProfileData().getSettingData().isTablistEnabled());

        player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.PROFILE_TOGGLED_TAB_LIST)
                .replace("{status}", profile.getProfileData().getSettingData().isTablistEnabled() ? "&aativou" : "&cdesativou"))
        );
    }
}
