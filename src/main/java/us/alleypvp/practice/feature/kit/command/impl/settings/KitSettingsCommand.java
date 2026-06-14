package us.alleypvp.practice.feature.kit.command.impl.settings;

import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.feature.kit.setting.KitSettingService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class KitSettingsCommand extends BaseCommand {
    @CommandData(
            name = "kit.settings",
            isAdminOnly = true,
            usage = "kit settings",
            description = "Lista todas as configurações de kit disponíveis."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        KitSettingService kitSettingService = this.plugin.getService(KitSettingService.class);

        player.sendMessage("");
        player.sendMessage(CC.translate("&b&lLista de Configurações de Kit &f(" + kitSettingService.getSettings().size() + "&f)"));
        if (kitSettingService.getSettings().isEmpty()) {
            player.sendMessage(CC.translate(" &f● &cNenhuma configuração de kit disponível."));
        }
        kitSettingService.getSettings().forEach(setting -> player.sendMessage(CC.translate(" &f◆ &b" + setting.getName() + " &8(&7" + setting.getDescription() + "&7)")));
        player.sendMessage("");
    }
}
