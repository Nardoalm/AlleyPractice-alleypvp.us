package us.alleypvp.practice.feature.arena.schematic.command;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import us.alleypvp.practice.feature.arena.schematic.ArenaSchematicService;
import us.alleypvp.practice.common.text.CC;
import org.bukkit.entity.Player;

import java.io.File;

/**
 * @author Emmy
 * @project Alley
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
        ArenaSchematicService schematicService = AlleyPractice.getInstance().getService(ArenaSchematicService.class);
        File schematicFile = schematicService.getSchematicFile(schematicName);

        if (!schematicFile.exists()) {
            player.sendMessage(CC.translate("&cSchematic file not found: &f" + schematicName + ".schematic"));
            return;
        }

        schematicService.paste(player.getLocation(), schematicFile);
        player.sendMessage(CC.translate("&aPasted schematic &e" + schematicName + "&a at your location."));
    }
}
