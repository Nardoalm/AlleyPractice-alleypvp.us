package com.kaosmc.practice.feature.match.command.admin.impl;

import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.arena.Arena;
import com.kaosmc.practice.feature.arena.ArenaService;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.kit.KitService;
import com.kaosmc.practice.feature.match.MatchService;
import com.kaosmc.practice.feature.match.model.GameParticipant;
import com.kaosmc.practice.feature.match.model.internal.MatchGamePlayer;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import com.kaosmc.practice.library.command.annotation.CompleterData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Kaos
 * @date 5/26/2024
 */
public class MatchStartCommand extends BaseCommand {
    @CompleterData(name = "match.start")
    public List<String> matchStartCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();
        Player player = command.getPlayer();

        if (player.hasPermission("alley.admin")) {
            switch (command.getArgs().length) {
                case 1:
                case 2:
                    for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
                        completion.add(onlinePlayer.getName());
                    }
                    break;
                case 3:
                    this.plugin.getService(KitService.class).getKits().forEach(kit -> completion.add(kit.getName()));
                    break;
                case 4:
                    Kit kit = this.plugin.getService(KitService.class).getKit(command.getArgs()[2]);
                    if (kit != null) {
                        this.plugin.getService(ArenaService.class).getArenas()
                                .stream()
                                .filter(arena -> arena.getKits().contains(kit.getName()))
                                .forEach(arena -> completion.add(arena.getName()));
                    }
                    break;
                default:
                    break;
            }
        }
        return completion;
    }

    @CommandData(
            name = "match.start",
            isAdminOnly = true,
            aliases = {"mstart"},
            cooldown = 1,
            usage = "match start <player1> <player2> <kit> <arena>",
            description = "Start a match between two players with a specific kit and arena"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 4) {
            command.sendUsage();
            return;
        }

        Player player1 = player.getServer().getPlayer(args[0]);
        Player player2 = player.getServer().getPlayer(args[1]);
        String kitName = args[2];
        String arenaName = args[3];

        if (player1 == null || player2 == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        Kit kit = this.plugin.getService(KitService.class).getKit(kitName);
        if (kit == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.KIT_NOT_FOUND).replace("{kit-name}", kitName));
            return;
        }

        Arena arena = this.plugin.getService(ArenaService.class).getArenaByName(arenaName);
        if (arena == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_NOT_FOUND).replace("{arena-name}", kitName));
            return;
        }

        MatchGamePlayer playerA = new MatchGamePlayer(player1.getUniqueId(), player1.getName());
        MatchGamePlayer playerB = new MatchGamePlayer(player2.getUniqueId(), player2.getName());

        GameParticipant<MatchGamePlayer> participantA = new GameParticipant<>(playerA);
        GameParticipant<MatchGamePlayer> participantB = new GameParticipant<>(playerB);

        this.plugin.getService(MatchService.class).createAndStartMatch(
                kit, this.plugin.getService(ArenaService.class).selectArenaWithPotentialTemporaryCopy(arena), participantA, participantB, false, false, false
        );
    }
}