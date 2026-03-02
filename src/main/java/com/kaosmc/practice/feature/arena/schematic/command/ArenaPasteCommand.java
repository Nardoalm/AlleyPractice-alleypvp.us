package com.kaosmc.practice.feature.arena.schematic.command;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import com.kaosmc.practice.feature.arena.schematic.ArenaSchematicService;
import com.kaosmc.practice.common.text.CC;
import org.bukkit.entity.Player;

import java.io.File;

/**
 * @author Emmy
 * @project Kaos
 * @since 16/06/2025
 */
public class ArenaPasteCommand extends BaseCommand {
    @CommandData(
            name = "arena.paste",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "arena paste <schematicName>",
            description = "Pastes a schematic at your location."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        String schematicName = args[0];
        ArenaSchematicService schematicService = KaosPractice.getInstance().getService(ArenaSchematicService.class);
        File schematicFile = schematicService.getSchematicFile(schematicName);

        if (!schematicFile.exists()) {
            player.sendMessage(CC.translate("&cSchematic file not found: &f" + schematicName + ".schematic"));
            return;
        }

        schematicService.paste(player.getLocation(), schematicFile);
        player.sendMessage(CC.translate("&aPasted schematic &e" + schematicName + "&a at your location."));
    }
}
