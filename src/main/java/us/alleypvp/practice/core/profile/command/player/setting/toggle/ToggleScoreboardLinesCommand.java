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
                .replace("{status}", profile.getProfileData().getSettingData().isShowScoreboardLines() ? "&aenable" : "&cdisable"))
        );
    }
}
