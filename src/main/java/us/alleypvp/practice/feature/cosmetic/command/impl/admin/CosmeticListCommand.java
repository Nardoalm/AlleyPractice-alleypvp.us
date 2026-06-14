package us.alleypvp.practice.feature.cosmetic.command.impl.admin;

import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.common.text.StringUtil;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.feature.cosmetic.CosmeticService;
import us.alleypvp.practice.feature.cosmetic.internal.repository.BaseCosmeticRepository;
import us.alleypvp.practice.feature.cosmetic.model.Cosmetic;
import us.alleypvp.practice.feature.cosmetic.model.CosmeticType;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
public class CosmeticListCommand extends BaseCommand {
    @CommandData(
            name = "cosmetic.list",
            isAdminOnly = true,
            usage = "cosmetic list",
            description = "Lista todos os cosméticos registrados."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Map<CosmeticType, BaseCosmeticRepository<?>> repositories = this.plugin.getService(CosmeticService.class).getRepositories();

        player.sendMessage("");

        if (repositories.isEmpty()) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.COSMETICS_NONE_REGISTERED));
            player.sendMessage("");
            return;
        }

        for (Map.Entry<CosmeticType, BaseCosmeticRepository<?>> entry : repositories.entrySet()) {
            CosmeticType type = entry.getKey();
            BaseCosmeticRepository<?> repository = entry.getValue();

            List<? extends Cosmetic> cosmetics = repository.getCosmetics();
            if (cosmetics.isEmpty()) {
                continue;
            }

            String friendlyTypeName = StringUtil.formatEnumName(type);
            String header = String.format("     &b&l%s &f(%d)", friendlyTypeName, cosmetics.size());
            player.sendMessage(CC.translate(header));

            for (Cosmetic cosmetic : cosmetics) {
                player.sendMessage(CC.translate("      &f◆ &b" + cosmetic.getName()));
            }
        }

        player.sendMessage("");
    }
}