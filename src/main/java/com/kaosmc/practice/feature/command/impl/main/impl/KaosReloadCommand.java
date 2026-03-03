package com.kaosmc.practice.feature.command.impl.main.impl;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.config.ConfigService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @date 06/06/2024 - 17:34
 */
public class KaosReloadCommand extends BaseCommand {

    //TODO: reload gui with options: Config files (messages), menus, Caches(kits, arenas, levels, etc..)

    @CommandData(
            name = "kaos.reload",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "kaos.reload",
            description = "Recarrega as configuracoes do Kaos."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        player.sendMessage(CC.translate("&6&lKaos &frecarregando..."));
        this.plugin.getService(ConfigService.class).reloadConfigs();
        player.sendMessage(CC.translate("&6&lKaos &a&lrecarregado&f."));
    }
}
