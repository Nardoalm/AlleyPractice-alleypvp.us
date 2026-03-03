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
 * @date 16/11/2024 - 23:14
 */
public class PartyOpenCommand extends BaseCommand {
    @CommandData(
            name = "party.open",
            aliases = {"p.open"},
            usage = "party open",
            description = "Abre sua party ao público."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getParty() == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_NOT_IN_PARTY));
            return;
        }

        if (!profile.getState().equals(ProfileState.LOBBY)) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_MUST_BE_IN_LOBBY));
            return;
        }

        profile.getParty().setState(PartyState.PUBLIC);
        player.sendMessage(CC.translate("&aVocê abriu sua party ao público. Agora qualquer um pode entrar."));
    }
}