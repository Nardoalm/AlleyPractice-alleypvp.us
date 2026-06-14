package us.alleypvp.practice.feature.tip.command;

import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import us.alleypvp.practice.feature.tip.Tip;
import us.alleypvp.practice.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 25/04/2025
 */
public class TipCommand extends BaseCommand {
    private final Tip tip;

    public TipCommand() {
        this.tip = new Tip();
    }

    @CommandData(
            name = "tip",
            aliases = {"tips"},
            usage = "tip",
            description = "Obter a random tip.",
            cooldown = 5
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        player.sendMessage(CC.translate(this.tip.getRandomTip()));
    }
}