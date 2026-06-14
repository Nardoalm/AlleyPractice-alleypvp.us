package us.alleypvp.practice.feature.party.command.impl.member;

import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import us.alleypvp.practice.feature.party.PartyService;
import us.alleypvp.practice.feature.party.Party;
import us.alleypvp.practice.feature.party.PartyState;
import us.alleypvp.practice.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 16/11/2024 - 23:25
 */
public class PartyJoinCommand extends BaseCommand {
    @CommandData(
            name = "party.join",
            aliases = {"p.join"},
            usage = "party join <player>",
            description = "Entra na party de um jogador."
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

        Party party = this.plugin.getService(PartyService.class).getPartyByLeader(target);
        if (party == null) {
            player.sendMessage(CC.translate("&cEsse jogador não está em uma party."));
            return;
        }

        if (party.getState() != PartyState.PUBLIC) {
            player.sendMessage(CC.translate("&cEssa party não está aberta ao público."));
            return;
        }

        this.plugin.getService(PartyService.class).joinParty(player, target);
    }
}