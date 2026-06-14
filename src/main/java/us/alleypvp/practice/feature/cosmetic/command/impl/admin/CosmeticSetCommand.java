package us.alleypvp.practice.feature.cosmetic.command.impl.admin;

import us.alleypvp.practice.common.text.EnumFormatter;
import us.alleypvp.practice.common.text.StringUtil;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.feature.cosmetic.CosmeticService;
import us.alleypvp.practice.feature.cosmetic.internal.repository.BaseCosmeticRepository;
import us.alleypvp.practice.feature.cosmetic.model.BaseCosmetic;
import us.alleypvp.practice.feature.cosmetic.model.CosmeticType;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
public class CosmeticSetCommand extends BaseCommand {
    @CommandData(
            name = "cosmetic.set",
            isAdminOnly = true,
            usage = "cosmetic set <player> <type> <cosmetic>",
            description = "Define o cosmético de um jogador."
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