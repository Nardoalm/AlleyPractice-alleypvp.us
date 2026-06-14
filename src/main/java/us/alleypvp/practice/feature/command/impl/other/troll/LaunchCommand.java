package us.alleypvp.practice.feature.command.impl.other.troll;

import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author Remi
 * @project Alley
 * @date 6/19/2024
 */
public class LaunchCommand extends BaseCommand {
    @CommandData(
            name = "launch",
            isAdminOnly = true,
            inGameOnly = false,
            description = "Lança um jogador",
            usage = "launch <player> | all"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            command.sendUsage();
            return;
        }

        if (args[0].equalsIgnoreCase("all")) {
            player.getServer().getOnlinePlayers().forEach(target -> target.setVelocity(new Vector(0, 1, 0).multiply(15)));
            player.sendMessage(CC.translate("&fVocê lançou todos os jogadores"));
            return;
        }

        Player targetPlayer = player.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        targetPlayer.setVelocity(new Vector(0, 1, 0).multiply(15));
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.TROLL_PLAYER_LAUNCHED)
                .replace("{name-color}", String.valueOf(this.getProfile(targetPlayer.getUniqueId()).getNameColor()))
                .replace("{player}", targetPlayer.getName())
        );
    }
}