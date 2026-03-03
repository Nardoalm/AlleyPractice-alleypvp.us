package com.kaosmc.practice.core.profile.data.command.coin;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Kaos
 * @date 6/2/2024
 */
public class SetCoinsCommand extends BaseCommand {
    @CommandData(
            name = "coins.set",
            isAdminOnly = true,
            usage = "coins set <player> <amount>",
            description = "Define as moedas de um jogador."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 2) {
            command.sendUsage();
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        Profile profile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());

        try {
            int amount = Integer.parseInt(args[1]);
            profile.getProfileData().setCoins(amount);
            player.sendMessage(CC.translate("&aSuccessfully set " + target.getName() + "'s coins to " + amount + "."));
        } catch (NumberFormatException e) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_NUMBER).replace("{input}", args[1]));
        }
    }
}
