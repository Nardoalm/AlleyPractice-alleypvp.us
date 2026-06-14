package us.alleypvp.practice.feature.party.command.impl.donator;

import us.alleypvp.practice.core.locale.LocaleService;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.enums.ProfileState;
import us.alleypvp.practice.feature.cooldown.Cooldown;
import us.alleypvp.practice.feature.cooldown.CooldownService;
import us.alleypvp.practice.feature.cooldown.CooldownType;
import us.alleypvp.practice.feature.party.PartyService;
import us.alleypvp.practice.feature.party.PartyState;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * @author Emmy
 * @project Alley
 * @date 17/11/2024 - 11:16
 */
public class PartyAnnounceCommand extends BaseCommand {
    @CommandData(
            name = "party.announce",
            aliases = {"p.announce"},
            permission = "kaos.donator.party.announce",
            usage = "party announce",
            description = "Anuncia sua party para o servidor."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        LocaleService localeService = this.plugin.getService(LocaleService.class);

        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getParty() == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_NOT_IN_PARTY));
            return;
        }

        if (!profile.getState().equals(ProfileState.LOBBY)) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_MUST_BE_IN_LOBBY));
            return;
        }

        if (profile.getParty().getState() != PartyState.PUBLIC) {
            player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.ERROR_YOU_PARTY_NOT_PUBLIC));
            return;
        }

        CooldownService cooldownService = this.plugin.getService(CooldownService.class);
        Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownService.getCooldown(player.getUniqueId(), CooldownType.PARTY_ANNOUNCE_COOLDOWN));
        if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
            player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.COOLDOWN_PARTY_ANNOUNCE_MUST_WAIT).replace("{time}", String.valueOf(optionalCooldown.get().remainingTimeInMinutes())));
            return;
        }

        Cooldown cooldown = optionalCooldown.orElseGet(() -> {
            Cooldown newCooldown = new Cooldown(CooldownType.PARTY_ANNOUNCE_COOLDOWN, () -> {
            });
            cooldownService.addCooldown(player.getUniqueId(), CooldownType.PARTY_ANNOUNCE_COOLDOWN, newCooldown);
            return newCooldown;
        });

        cooldown.resetCooldown();

        this.plugin.getService(PartyService.class).announceParty(profile.getParty());
    }
}