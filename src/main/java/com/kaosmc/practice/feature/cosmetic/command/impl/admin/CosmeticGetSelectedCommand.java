package com.kaosmc.practice.feature.cosmetic.command.impl.admin;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.common.text.StringUtil;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.data.types.ProfileCosmeticData;
import com.kaosmc.practice.feature.cosmetic.model.CosmeticType;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Kaos
 * @date 6/1/2024
 */
public class CosmeticGetSelectedCommand extends BaseCommand {
    @CommandData(
            name = "cosmetic.getselected",
            aliases = {"cosmetic.get"},
            isAdminOnly = true,
            usage = "cosmetic get <player>",
            description = "Get the selected cosmetics of a player."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            command.sendUsage();
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        Profile profile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());
        player.sendMessage(CC.translate("     &6&lSelected Cosmetics for " + target.getName()));

        ProfileCosmeticData cosmeticData = profile.getProfileData().getCosmeticData();

        for (CosmeticType type : CosmeticType.values()) {
            String selectedName = cosmeticData.getSelected(type);

            String friendlyTypeName = StringUtil.formatEnumName(type);

            player.sendMessage(CC.translate(String.format("      &f◆ &6%s: &f%s", friendlyTypeName, selectedName)));
        }
    }
}