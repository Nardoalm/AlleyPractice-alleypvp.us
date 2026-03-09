package com.kaosmc.practice.feature.queue.menu.button;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.feature.arena.Arena;
import com.kaosmc.practice.feature.arena.ArenaService;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Kaos
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
                        "&7Kit: &6" + kit.getName(),
                        "",
                        "&7Clique para iniciar a luta!"
                )
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Arena arena = KaosPractice.getInstance().getService(ArenaService.class).getRandomArena(this.kit);
        if (arena == null) {
            player.sendMessage("Nenhuma arena disponível.");
            return;
        }

        player.sendMessage(CC.translate("&cEste recurso ainda não está disponível."));
    }
}
