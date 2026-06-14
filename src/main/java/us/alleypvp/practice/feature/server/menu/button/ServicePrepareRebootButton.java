package us.alleypvp.practice.feature.server.menu.button;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.library.menu.Button;
import us.alleypvp.practice.feature.server.ServerService;
import us.alleypvp.practice.common.item.ItemBuilder;
import us.alleypvp.practice.common.text.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 09/03/2025
 */
public class ServicePrepareRebootButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.REDSTONE)
                .name("&c&lPreparar Reinício")
                .lore(
                        "&fIsso preparará o servidor",
                        "&fpara um reinício, cancelando",
                        "&ftarefas e partidas em andamento.",
                        "",
                        "&cAVISO:",
                        " &fNão haverá menu",
                        " &fde confirmação.",
                        "",
                        "&cClique para preparar!"
                )
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        if (!player.isOp()) {
            player.sendMessage(CC.translate("&cVocê não tem permissão para usar este botão."));
            return;
        }

        ServerService serverService = AlleyPractice.getInstance().getService(ServerService.class);

        if (!serverService.isQueueingAllowed()) {
            player.sendMessage(CC.translate("&cO servidor já está se preparando para reiniciar."));
            return;
        }

        serverService.endAllMatches(player);
        serverService.disbandAllParties(player);
        serverService.clearAllQueues(player);

        serverService.setQueueingAllowed(false);
        Arrays.asList(
                "",
                "&c&lO servidor está se preparando para reiniciar...",
                ""
        ).forEach(line -> Bukkit.broadcastMessage(CC.translate(line)));
    }
}
