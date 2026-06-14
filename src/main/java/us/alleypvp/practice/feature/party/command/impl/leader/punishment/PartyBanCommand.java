package us.alleypvp.practice.feature.party.command.impl.leader.punishment;

import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import us.alleypvp.practice.feature.party.PartyService;
import us.alleypvp.practice.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 11/12/2024 - 13:25
 */
public class PartyBanCommand extends BaseCommand {
    @CommandData(
            name = "party.ban",
            aliases = "p.ban",
            usage = "party ban <player>",
            description = "Ban a player from your party."
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
            player.sendMessage(CC.translate("&cVocê não pode se banir da party."));
            return;
        }

        this.plugin.getService(PartyService.class).banMember(player, target);
    }
}