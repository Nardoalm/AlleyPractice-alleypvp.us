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
 * @since 19/07/2025
 */
public class ToggleServerTitlesCommand extends BaseCommand {
    @CommandData(
            name = "toggleservertitles",
            cooldown = 1,
            usage = "toggleservertitles",
            description = "Ativa ou desativa os títulos do servidor"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = this.getProfile(player.getUniqueId());
        profile.getProfileData().getSettingData().setServerTitles(!profile.getProfileData().getSettingData().isServerTitles());

        player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.PROFILE_TOGGLED_SERVER_TITLES)
                .replace("{status}", profile.getProfileData().getSettingData().isServerTitles() ? "&aenabled" : "&cdisabled"))
        );
    }
}