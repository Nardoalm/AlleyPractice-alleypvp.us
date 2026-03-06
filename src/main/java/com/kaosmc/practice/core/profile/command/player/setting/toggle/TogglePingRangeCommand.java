package com.kaosmc.practice.core.profile.command.player.setting.toggle;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.data.types.ProfileSettingData;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * Cycles a player's matchmaking ping range setting.
 */
public class TogglePingRangeCommand extends BaseCommand {
    @CommandData(
            name = "togglepingrange",
            cooldown = 1,
            usage = "togglepingrange",
            description = "Alterna o limite de diferença de ping no matchmaking."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        if (player == null) {
            return;
        }

        Profile profile = this.getProfile(player.getUniqueId());
        if (profile == null || profile.getProfileData() == null) {
            player.sendMessage(CC.translate("&cSeu perfil ainda não foi carregado."));
            return;
        }

        ProfileSettingData settingData = profile.getProfileData().getSettingData();
        if (settingData == null) {
            player.sendMessage(CC.translate("&cSuas configurações não estão disponíveis no momento."));
            return;
        }

        settingData.cyclePingRange();
        player.sendMessage(CC.translate("&aPing range do matchmaking: &6" + settingData.getPingRangeDisplay() + "&a."));
    }
}
