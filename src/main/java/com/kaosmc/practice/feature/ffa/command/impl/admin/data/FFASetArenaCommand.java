package com.kaosmc.practice.feature.ffa.command.impl.admin.data;

import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.arena.Arena;
import com.kaosmc.practice.feature.arena.ArenaService;
import com.kaosmc.practice.feature.arena.ArenaType;
import com.kaosmc.practice.feature.ffa.FFAService;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.kit.KitService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project kaos-practice
 * @since 25/07/2025
 */
public class FFASetArenaCommand extends BaseCommand {
    @CommandData(
            name = "ffa.setarena",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "ffa setarena <kitName> <arenaName>",
            description = "Define a arena de FFA para um kit."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            command.sendUsage();
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.KIT_NOT_FOUND));
            return;
        }

        String arenaName = args[1];
        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        Arena arena = arenaService.getArenaByName(arenaName);
        if (arena == null) {
            sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_NOT_FOUND).replace("{arena-name}", arenaName));
            return;
        }

        if (arena.getType() != ArenaType.FFA) {
            sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.FFA_CAN_ONLY_SETUP_IN_FFA_ARENA));
            return;
        }

        if (!kit.isFfaEnabled()) {
            sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.FFA_DISABLED).replace("{kit-name}", kit.getName()));
            return;
        }

        kit.setFfaArenaName(arena.getName());
        kitService.saveKit(kit);
        this.plugin.getService(FFAService.class).reloadFFAKits();
        sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.FFA_ARENA_SET)
                .replace("{kit-name}", kit.getName())
                .replace("{arena-name}", arena.getName())
        );
    }
}