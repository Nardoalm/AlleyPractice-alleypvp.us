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
 * @since 03/03/2025
 */
public class ToggleScoreboardLinesCommand extends BaseCommand {
    @CommandData(
            name = "togglescoreboardlines",
            aliases = "tsl",
            cooldown = 1,
            usage = "togglescoreboardlines",
            description = "Alterna as linhas da scoreboard."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = this.getProfile(player.getUniqueId());
        profile.getProfileData().getSettingData().setShowScoreboardLines(!profile.getProfileData().getSettingData().isShowScoreboardLines());

        player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.PROFILE_TOGGLED_SCOREBOARD_LINES)
                .replace("{status}", profile.getProfileData().getSettingData().isShowScoreboardLines() ? "&aativou" : "&cdesativou"))
        );
    }
}
