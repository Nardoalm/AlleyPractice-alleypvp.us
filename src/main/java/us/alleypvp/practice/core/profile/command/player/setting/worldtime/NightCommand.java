package us.alleypvp.practice.core.profile.command.player.setting.worldtime;

import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 02/06/2024 - 11:02
 */
public class NightCommand extends BaseCommand {
    @CommandData(
            name = "night",
            cooldown = 1,
            usage = "night",
            description = "Define o horário pessoal do mundo para noite."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = this.getProfile(player.getUniqueId());

        profile.getProfileData().getSettingData().setTimeNight(player);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.PROFILE_WORLD_TIME_SET)
                .replace("{time}", profile.getProfileData().getSettingData().getTime().toLowerCase())
        );
    }
}