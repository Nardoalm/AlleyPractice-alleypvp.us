package com.kaosmc.practice.feature.cosmetic.command.impl.admin;

import com.kaosmc.practice.common.text.EnumFormatter;
import com.kaosmc.practice.common.text.StringUtil;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.feature.cosmetic.CosmeticService;
import com.kaosmc.practice.feature.cosmetic.internal.repository.BaseCosmeticRepository;
import com.kaosmc.practice.feature.cosmetic.model.BaseCosmetic;
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
public class CosmeticSetCommand extends BaseCommand {
    @CommandData(
            name = "cosmetic.set",
            isAdminOnly = true,
            usage = "cosmetic set <player> <type> <cosmetic>",
            description = "Set a player's cosmetic."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 3) {
            command.sendUsage();
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        Profile profile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());

        String typeName = args[1];
        String cosmeticName = args[2];

        CosmeticType cosmeticType = CosmeticType.fromString(typeName);
        if (cosmeticType == null) {
            player.sendMessage(EnumFormatter.outputAvailableValues(CosmeticType.class));
            return;
        }

        BaseCosmeticRepository<?> repository = this.plugin.getService(CosmeticService.class).getRepository(cosmeticType);
        if (repository == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.COSMETIC_TYPE_NOT_SUPPORTED).replace("{type}", typeName));
            return;
        }

        BaseCosmetic cosmetic = repository.getCosmetic(cosmeticName);
        if (cosmetic == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.COSMETIC_NOT_FOUND).replace("{input}", cosmeticName));
            return;
        }

        profile.getProfileData().getCosmeticData().setSelected(cosmetic);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.COSMETIC_SET_FOR_PLAYER)
                .replace("{type}", StringUtil.formatEnumName(cosmeticType))
                .replace("{cosmetic}", cosmetic.getName())
                .replace("{player}", target.getName())
        );
    }
}