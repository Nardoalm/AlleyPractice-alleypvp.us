package com.kaosmc.practice.feature.party.command.impl.leader.punishment;

import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import com.kaosmc.practice.feature.party.PartyService;
import com.kaosmc.practice.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @date 11/12/2024 - 13:33
 */
public class PartyUnbanCommand extends BaseCommand {
    @CommandData(
            name = "party.unban",
            aliases = "p.unban",
            usage = "party unban <player>",
            description = "Unban a player from your party."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cEsse jogador não está online."));
            return;
        }

        if (target.equals(player)) {
            player.sendMessage(CC.translate("&cVocê não pode se desbanir da party."));
            return;
        }

        this.plugin.getService(PartyService.class).unbanMember(player, target);
    }
}