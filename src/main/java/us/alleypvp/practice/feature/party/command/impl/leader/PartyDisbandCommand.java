package us.alleypvp.practice.feature.party.command.impl.leader;

import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.feature.party.PartyService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 22/05/2024 - 20:36
 */
public class PartyDisbandCommand extends BaseCommand {
    @Override
    @CommandData(
            name = "party.disband",
            aliases = {"p.disband"},
            usage = "party disband",
            description = "Desfaz sua party."
    )
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        PartyService partyService = this.plugin.getService(PartyService.class);
        if (partyService.getPartyByLeader(player) != null) {
            partyService.disbandParty(player);
            //player.sendMessage(CC.translate(PartyLocale.PARTY_DISBANDED.getMessage()));
            return;
        }

        player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_NOT_PARTY_LEADER)));
    }
}
