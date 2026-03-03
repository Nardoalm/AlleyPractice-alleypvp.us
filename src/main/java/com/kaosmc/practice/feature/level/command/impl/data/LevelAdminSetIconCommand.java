package com.kaosmc.practice.feature.level.command.impl.data;

import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.level.LevelService;
import com.kaosmc.practice.feature.level.data.LevelData;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @since 26/05/2025
 */
public class LevelAdminSetIconCommand extends BaseCommand {
    @CommandData(
            name = "leveladmin.seticon",
            isAdminOnly = true,
            usage = "leveladmin seticon <levelName>",
            description = "Define o ícone de um nível"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        String levelName = args[0];
        LevelService levelService = this.plugin.getService(LevelService.class);
        LevelData level = levelService.getLevel(levelName);
        if (level == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.LEVEL_NOT_FOUND).replace("{level-name}", levelName));
            return;
        }

        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_MUST_HOLD_ITEM));
            return;
        }

        Material iconMaterial = player.getItemInHand().getType();
        int durability = player.getItemInHand().getDurability();
        level.setMaterial(iconMaterial);
        level.setDurability(durability);
        levelService.saveLevel(level);

        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.LEVEL_ICON_SET)
                .replace("{level-name}", levelName)
                .replace("{icon-material}", iconMaterial.name())
        );
    }
}