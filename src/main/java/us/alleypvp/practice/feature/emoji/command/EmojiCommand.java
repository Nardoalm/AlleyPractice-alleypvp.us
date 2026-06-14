package us.alleypvp.practice.feature.emoji.command;

import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.common.text.ClickableUtil;
import us.alleypvp.practice.feature.emoji.EmojiType;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @date 22/01/2025 - 21:15
 */
public class EmojiCommand extends BaseCommand {
    @CommandData(
            name = "emoji",
            aliases = "emojis",
            permission = "kaos.donator.chat.symbol",
            usage = "emoji",
            description = "Guia de ajuda para emojis"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Arrays.asList(
                "",
                "&b&lEmoji Help",
                " &7To use an emoji, simply type the identifier in chat.",
                " &7For example, if you want to use the &f" + EmojiType.HEART.getFormat() + " &7emoji, type &f" + EmojiType.HEART.getIdentifier() + "&7 in chat.",
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));

        player.spigot().sendMessage(ClickableUtil.createComponent(" &a[Clique aqui para ver todos os emojis]", "/emoji list", "&7Clique aqui para ver a lista de todos os emojis disponíveis."));
        player.sendMessage("");
    }
}