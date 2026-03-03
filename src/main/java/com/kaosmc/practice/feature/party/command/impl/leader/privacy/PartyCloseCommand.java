package com.kaosmc.practice.feature.party.command.impl.leader.privacy;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.enums.ProfileState;
import com.kaosmc.practice.feature.party.PartyState;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @date 16/11/2024 - 23:16
 */
public class PartyCloseCommand extends BaseCommand {
    @CommandData(
            name = "party.close",
            aliases = {"p.close"},
            usage = "party close",
            description = "Fecha sua party para novos membros."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getParty() == null) {
            player.sendMessage(CC.translate("&cVocê não está em uma party."));
            return;
        }

        if (!profile.getState().equals(ProfileState.LOBBY)) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_MUST_BE_IN_LOBBY));
            return;
        }

        profile.getParty().setState(PartyState.PRIVATE);
        player.sendMessage(CC.translate("&aVocê fechou sua party. Ninguém pode entrar sem convite."));
    }
}