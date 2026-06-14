package us.alleypvp.practice.feature.command.impl.main.impl;

import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.core.config.ConfigService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 06/06/2024 - 17:34
 */
public class KaosReloadCommand extends BaseCommand {

    //TODO: reload gui with options: Config files (messages), menus, Caches(kits, arenas, levels, etc..)

    @CommandData(
            name = "kaos.reload",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "kaos.reload",
            description = "Recarrega as configuracoes do Alley."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        player.sendMessage(CC.translate("&b&lKaos &frecarregando..."));
        this.plugin.getService(ConfigService.class).reloadConfigs();
        player.sendMessage(CC.translate("&b&lKaos &a&lrecarregado&f."));
    }
}
