package com.kaosmc.practice.feature.ffa.command.impl.admin.data;

import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.arena.Arena;
import com.kaosmc.practice.feature.arena.ArenaService;
import com.kaosmc.practice.feature.arena.ArenaType;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @since 11/04/2025
 */
public class FFASetSpawnCommand extends BaseCommand {
    @CommandData(
            name = "ffa.setspawn",
            isAdminOnly = true,
            usage = "ffa setspawn <arenaName> <spawnNumber>",
            description = "Define um ponto de spawn para uma arena de FFA."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        Arena arena = arenaService.getArenaByName(args[0]);
        if (arena == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_NOT_FOUND).replace("{arena-name}", args[0]));
            return;
        }

        if (arena.getType() != ArenaType.FFA) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.FFA_CAN_ONLY_SETUP_IN_FFA_ARENA));
            return;
        }

        arena.setPos1(player.getLocation());
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.FFA_SPAWN_SET).replace("{arena-name}", arena.getName()));
    }
}