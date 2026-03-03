package com.kaosmc.practice.feature.queue.command.player;

import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import com.kaosmc.practice.feature.queue.QueueService;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.enums.ProfileState;
import com.kaosmc.practice.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @date 25/05/2024 - 16:45
 */
public class QueuesCommand extends BaseCommand {
    @Override
    @CommandData(
            name = "queues",
            aliases = {"selectqueue", "joinqueue"},
            usage = "queues",
            description = "Abre o menu de seleção de fila."
    )
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (!profile.getState().equals(ProfileState.LOBBY)) {
            player.sendMessage(CC.translate("&cVocê precisa estar no spawn para executar este comando :v"));
            return;
        }

        if (profile.getParty() != null) {
            player.sendMessage(CC.translate("&cVocê precisa sair da party para entrar na fila de uma partida."));
            return;
        }

        this.plugin.getService(QueueService.class).getQueueMenu().openMenu(player);
    }
}
