package com.kaosmc.practice.feature.queue.command.admin.impl;

import com.kaosmc.practice.common.PlayerUtil;
import com.kaosmc.practice.common.SoundUtil;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.feature.hotbar.HotbarService;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.kit.KitService;
import com.kaosmc.practice.feature.queue.Queue;
import com.kaosmc.practice.feature.queue.QueueService;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Kaos
 * @date 5/26/2024
 */
public class QueueForceCommand extends BaseCommand {
    @CommandData(
            name = "queue.force",
            aliases = {"forcequeue"},
            isAdminOnly = true,
            usage = "queue force <player> <kit> <ranked>",
            description = "Força um jogador para uma fila."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 3) {
            command.sendUsage();
            player.sendMessage(CC.translate("&7Exemplo: /queue force hmRemi Boxing true"));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        String kitType = args[1];
        boolean ranked = Boolean.parseBoolean(args[2]);

        if (target == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        Kit kit = this.plugin.getService(KitService.class).getKit(kitType);
        if (kit == null) {
            player.sendMessage(CC.translate("&cKit não encontrado."));
            return;
        }

        Profile profile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());
        for (Queue queue : this.plugin.getService(QueueService.class).getQueues()) {
            if (queue.getKit().equals(kit) && queue.isRanked() == ranked) {
                queue.addPlayer(target, queue.isRanked() ? profile.getProfileData().getRankedKitData().get(queue.getKit().getName()).getElo() : 0);
                PlayerUtil.reset(target, false, true);
                SoundUtil.playBanHammer(target);
                this.plugin.getService(HotbarService.class).applyHotbarItems(target);

                player.sendMessage(this.getString(GlobalMessagesLocaleImpl.QUEUE_FORCED_PLAYER)
                        .replace("{player}", target.getName())
                        .replace("{kit}", kit.getName())
                        .replace("{ranked}", ranked ? "ranked" : "unranked")
                );

                if (ranked && profile.getProfileData().isRankedBanned()) {
                    player.sendMessage(CC.translate("&cKeep in mind that &6" + target.getName() + " &cis currently banned from ranked queues!"));
                }

                return;
            }
        }
    }
}
