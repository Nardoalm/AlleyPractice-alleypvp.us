package com.kaosmc.practice.feature.tip.command;

import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import com.kaosmc.practice.feature.tip.Tip;
import com.kaosmc.practice.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
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
            description = "Get a random tip.",
            cooldown = 5
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        player.sendMessage(CC.translate(this.tip.getRandomTip()));
    }
}