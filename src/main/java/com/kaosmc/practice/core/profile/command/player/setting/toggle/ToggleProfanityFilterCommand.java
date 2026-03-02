package com.kaosmc.practice.core.profile.command.player.setting.toggle;

import com.kaosmc.practice.common.constants.MessageConstant;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.SettingsLocaleImpl;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @since 27/04/2025
 */
public class ToggleProfanityFilterCommand extends BaseCommand {
    @CommandData(
            name = "toggleprofanityfilter",
            aliases = {"tpf"},
            cooldown = 1,
            usage = "toggleprofanityfilter",
            description = "Toggle the profanity filter on or off."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = this.getProfile(player.getUniqueId());
        profile.getProfileData().getSettingData().setProfanityFilterEnabled(!profile.getProfileData().getSettingData().isProfanityFilterEnabled());

        boolean chatFormatEnabled = this.getBoolean(SettingsLocaleImpl.SERVER_CHAT_FORMAT_ENABLED_BOOLEAN);
        if (!chatFormatEnabled) {
            player.sendMessage(MessageConstant.FEATURE_CURRENTLY_DISABLED);
            if (player.isOp()) {
                player.sendMessage(CC.translate("&cYou seem to be an operator. This feature is disabled because you're currently &c&lnot &cusing &6&lAlley's &cchat format. &7To enable it, head to settings config and enable chat format."));
            }
            return;
        }

        player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.PROFILE_TOGGLED_PROFANITY_FILTER)
                .replace("{status}", profile.getProfileData().getSettingData().isProfanityFilterEnabled() ? "&aenabled" : "&cdisabled"))
        );
    }
}