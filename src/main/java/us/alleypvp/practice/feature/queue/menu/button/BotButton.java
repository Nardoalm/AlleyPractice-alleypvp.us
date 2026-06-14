package us.alleypvp.practice.feature.queue.menu.button;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.library.menu.Button;
import us.alleypvp.practice.feature.arena.Arena;
import us.alleypvp.practice.feature.arena.ArenaService;
import us.alleypvp.practice.feature.kit.Kit;
import us.alleypvp.practice.common.item.ItemBuilder;
import us.alleypvp.practice.common.text.CC;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @since 16/04/2025
 */
@AllArgsConstructor
public class BotButton extends Button {
    private final Kit kit;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(kit.getIcon())
                .name(kit.getName())
                //TODO: This is just temporary, we're going to make this dynamic for all type of bots, not only a statically defined one. FOR TESTING PURPOSES ONLY.
                .lore(
                        "&7Clique para lutar contra um bot!",
                        "",
                        "&7Kit: &b" + kit.getName(),
                        "",
                        "&7Clique para iniciar a luta!"
                )
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Arena arena = AlleyPractice.getInstance().getService(ArenaService.class).getRandomArena(this.kit);
        if (arena == null) {
            player.sendMessage("Nenhuma arena disponível.");
            return;
        }

        player.sendMessage(CC.translate("&cEste recurso ainda não está disponível."));
    }
}
