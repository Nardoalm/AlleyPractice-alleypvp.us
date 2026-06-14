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
                .replace("{status}", profile.getProfileData().getSettingData().isServerTitles() ? "&aenable" : "&cdisable"))
        );
    }
}
