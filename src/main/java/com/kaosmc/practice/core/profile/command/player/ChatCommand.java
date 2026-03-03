package com.kaosmc.practice.core.profile.command.player;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.enums.ChatChannel;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @date 22/10/2024 - 12:14
 */
public class ChatCommand extends BaseCommand {
    @CommandData(
            name = "chat",
            usage = "chat <chatChannel>",
            description = "Definir your chat channel."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            player.sendMessage(CC.translate("&cCanais de chat disponíveis: " + ChatChannel.getChatChannelsSorted()));
            return;
        }

        Profile profile = this.getProfile(player.getUniqueId());
        if (ChatChannel.getExactChatChannel(args[0], true) == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.CHAT_CHANNEL_NOT_EXIST).replace("{channel}", args[0]));
            return;
        }

        if (profile.getProfileData().getSettingData().getChatChannel().equalsIgnoreCase(args[0])) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.CHAT_CHANNEL_ALREADY_IN).replace("{channel}", args[0]));
            return;
        }

        profile.getProfileData().getSettingData().setChatChannel(ChatChannel.getExactChatChannel(args[0], true));
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.CHAT_CHANNEL_SET).replace("{channel}", args[0]));
    }
}