package com.kaosmc.practice.feature.emoji.command.impl;

import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import com.kaosmc.practice.feature.emoji.EmojiType;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.common.text.Symbol;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @date 22/01/2025 - 21:52
 */
public class EmojiListCommand extends BaseCommand {
    @CommandData(name = "emoji.list", aliases = "el", permission = "kaos.donator.chat.symbol", usage = "emoji list", description = "lista todos os emojis disponíveis para uso no chat")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("&6&lEmojis: &7(Apelido: &f/el&7)"));
        for (EmojiType emoji : EmojiType.values()) {
            TextComponent emojiComponent = new TextComponent(CC.translate(" " + emoji.getIdentifier() + " &7" + Symbol.ARROW_R + " &f" + emoji.getFormat()));
            emojiComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, emoji.getIdentifier()));

            String hoverText = "&7Clique para aplicar &f" + emoji.getFormat() + " &7na entrada do chat.";
            BaseComponent[] hoverComponent = new ComponentBuilder(CC.translate(hoverText)).create();
            emojiComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent));

            player.spigot().sendMessage(emojiComponent);
        }
        player.sendMessage("");
    }
}
