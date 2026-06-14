package us.alleypvp.practice.feature.cosmetic.command.impl.admin;

import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.common.text.StringUtil;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.data.types.ProfileCosmeticData;
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
public class CosmeticGetSelectedCommand extends BaseCommand {
    @CommandData(
            name = "cosmetic.getselected",
            aliases = {"cosmetic.get"},
            isAdminOnly = true,
            usage = "cosmetic get <player>",
            description = "Obtém os cosméticos selecionados de um jogador."
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
        player.sendMessage(CC.translate("     &b&lCosméticos selecionados de " + target.getName()));

        ProfileCosmeticData cosmeticData = profile.getProfileData().getCosmeticData();

        for (CosmeticType type : CosmeticType.values()) {
            String selectedName = cosmeticData.getSelected(type);

            String friendlyTypeName = StringUtil.formatEnumName(type);

            player.sendMessage(CC.translate(String.format("      &f◆ &b%s: &f%s", friendlyTypeName, selectedName)));
        }
    }
}
