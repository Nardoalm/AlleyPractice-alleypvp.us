package us.alleypvp.practice.feature.cooldown.command;

import us.alleypvp.practice.common.text.EnumFormatter;
import us.alleypvp.practice.common.text.StringUtil;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.feature.cooldown.Cooldown;
import us.alleypvp.practice.feature.cooldown.CooldownService;
import us.alleypvp.practice.feature.cooldown.CooldownType;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 24/06/2025
 */
public class CooldownResetCommand extends BaseCommand {
    @CommandData(
            name = "cooldown.reset",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "cooldown reset <player> <cooldown>",
            description = "Reseta o cooldown de um jogador de um tipo específico."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            command.sendUsage();
            return;
        }

        String targetName = args[0];
        Player target = this.plugin.getServer().getPlayer(targetName);
        if (target == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        CooldownType type;
        try {
            type = CooldownType.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(EnumFormatter.outputAvailableValues(CooldownType.class));
            return;
        }

        CooldownService repository = this.plugin.getService(CooldownService.class);
        Cooldown cooldown = repository.getCooldown(target.getUniqueId(), type);
        if (cooldown == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.COOLDOWN_NOT_FOUND)
                    .replace("{player-name}", target.getName())
                    .replace("{cooldown-type}", StringUtil.formatEnumName(type))
            );
            return;
        }

        repository.removeCooldown(player.getUniqueId(), type);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.COOLDOWN_RESET)
                .replace("{player-name}", target.getName())
                .replace("{cooldown-type}", StringUtil.formatEnumName(type))
        );
    }
}