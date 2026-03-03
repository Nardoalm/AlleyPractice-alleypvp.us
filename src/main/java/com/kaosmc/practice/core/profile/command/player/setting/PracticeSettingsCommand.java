package com.kaosmc.practice.core.profile.command.player.setting;

import com.kaosmc.practice.core.profile.menu.setting.PracticeSettingsMenu;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @date 19/05/2024 - 11:27
 */

public class PracticeSettingsCommand extends BaseCommand {
    @CommandData(
            name = "practicesettings",
            usage = "practicesettings",
            description = "Abre o menu de configurações do practice."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new PracticeSettingsMenu().openMenu(player);
    }
}
