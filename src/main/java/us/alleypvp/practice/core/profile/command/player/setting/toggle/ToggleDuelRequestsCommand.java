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
                .replace("{status}", profile.getProfileData().getSettingData().isReceiveDuelRequestsEnabled() ? "&aenable" : "&cdisable"))
        );
    }
}
