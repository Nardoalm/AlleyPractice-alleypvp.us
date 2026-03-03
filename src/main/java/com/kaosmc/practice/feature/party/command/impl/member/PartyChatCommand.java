package com.kaosmc.practice.feature.party.command.impl.member;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.enums.ChatChannel;
import com.kaosmc.practice.feature.party.PartyService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Remi
 * @project Kaos
 * @date 5/25/2024
 */
public class PartyChatCommand extends BaseCommand {
    @CommandData(
            name = "party.chat",
            aliases = {"p.chat", "pc"},
            usage = "party chat [message]",
            description = "Alterna ou envia mensagem no chat da party."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        String message = Arrays.stream(args).map(argument -> argument + " ").collect(Collectors.joining());

        if (args.length == 0) {
            if (profile.getProfileData().getSettingData().getChatChannel().equals(ChatChannel.PARTY.toString())) {
                profile.getProfileData().getSettingData().setChatChannel(ChatChannel.GLOBAL.toString());
                player.sendMessage(CC.translate("&aSeu canal de chat foi definido para &6global&a."));
            } else {
                profile.getProfileData().getSettingData().setChatChannel(ChatChannel.PARTY.toString());
                player.sendMessage(CC.translate("&aSeu canal de chat foi definido para &6party&a."));
            }
            return;
        }

        if (profile.getParty() == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_NOT_IN_PARTY));
            return;
        }

        if (!profile.getProfileData().getSettingData().isPartyMessagesEnabled()) {
            player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_PARTY_CHAT_DISABLED)));
            return;
        }

        String partyMessage = this.plugin.getService(PartyService.class).getChatFormat().replace("{player}", player.getName()).replace("{message}", message);
        profile.getParty().notifyParty(partyMessage);
    }
}