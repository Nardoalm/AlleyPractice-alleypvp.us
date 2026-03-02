package com.kaosmc.practice.core.profile.data.command.ranked;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Kaos
 * @since 13/03/2025
 */
public class RankedCommand extends BaseCommand {

    //TODO: Menu for managing ranked bans? reason? duration?

    @CommandData(
            name = "ranked",
            isAdminOnly = true,
            usage = "ranked",
            description = "Manage ranked allowance."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Arrays.asList(
                " ",
                "&6&lRanked Commands Help:",
                " &f◆ &6/ranked ban &8(&7player&8) &7| Ban a player from ranked matches.",
                " &f◆ &6/ranked unban &8(&7player&8) &7| Unban a player from ranked matches.",
                " "
        ).forEach(message -> player.sendMessage(CC.translate(message)));
    }
}