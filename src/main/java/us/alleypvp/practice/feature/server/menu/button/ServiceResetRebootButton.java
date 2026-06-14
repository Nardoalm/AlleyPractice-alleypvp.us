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
public class ServiceResetRebootButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.EMERALD)
                .name("&a&lCancelar Reinício")
                .lore(
                        "&fIsso cancelará a preparação",
                        "&fpara um reinício, liberando",
                        "&fas filas novamente.",
                        "",
                        "&aClique para liberar as filas novamente!"
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

        AlleyPractice.getInstance().getService(ServerService.class).setQueueingAllowed(true);
        Arrays.asList(
                "",
                "&a&lAS FILAS NÃO ESTÃO MAIS DESATIVADAS!",
                ""
        ).forEach(line -> Bukkit.broadcastMessage(CC.translate(line)));
    }
}
