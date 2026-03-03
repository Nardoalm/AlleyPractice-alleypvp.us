package com.kaosmc.practice.core.profile.command.player.setting.worldtime;

import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @date 02/06/2024 - 11:05
 */
public class SunsetCommand extends BaseCommand {
    @CommandData(
            name = "sunset",
            aliases = "sunrise",
            cooldown = 1,
            usage = "sunset",
            description = "Define o horário pessoal do mundo para pôr do sol."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = this.getProfile(player.getUniqueId());

        profile.getProfileData().getSettingData().setTimeSunset(player);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.PROFILE_WORLD_TIME_SET)
                .replace("{time}", profile.getProfileData().getSettingData().getTime().toLowerCase())
        );
    }
}