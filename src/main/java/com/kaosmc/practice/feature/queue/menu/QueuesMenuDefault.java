package com.kaosmc.practice.feature.queue.menu;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.constants.MessageConstant;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.feature.ffa.menu.FFAMenu;
import com.kaosmc.practice.feature.queue.QueueService;
import com.kaosmc.practice.feature.queue.menu.sub.UnrankedMenu;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.library.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project Kaos
 * @date 23/05/2024 - 01:28
 */
@AllArgsConstructor
public class QueuesMenuDefault extends Menu {
    protected final KaosPractice plugin;

    public QueuesMenuDefault() {
        this.plugin = KaosPractice.getInstance();
    }

    /**
     * Get the title of the menu.
     *
     * @param player the player to get the title for
     * @return the title of the menu
     */
    @Override
    public String getTitle(Player player) {
        return "&6&lFilas Solo Unranked";
    }

    /**
     * Get the buttons for the menu.
     *
     * @param player the player to get the buttons for
     * @return the buttons for the menu
     */
    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        QueueService queueService = KaosPractice.getInstance().getService(QueueService.class);

        buttons.put(11, new QueuesButtonDefault("&6&lSolos", Material.DIAMOND_SWORD, 0, Arrays.asList(
                "&71v1 casuais sem",
                "&7penalidade por derrota.",
                "",
                "&6Jogadores: &f" + queueService.getPlayerCountOfGameType("Unranked"),
                "",
                "&aClique para jogar!"
        )));

        buttons.put(13, new QueuesButtonDefault("&6&lBots", Material.GOLD_SWORD, 0, Arrays.asList(
                "&7Pratique contra bots",
                "&7para melhorar sua habilidade.",
                "",
                "&6Jogadores: &f" + queueService.getPlayerCountOfGameType("Bots"),
                "",
                "&aClique para jogar!"
        )));

        buttons.put(15, new QueuesButtonDefault("&6&lFFA", Material.GOLD_AXE, 0, Arrays.asList(
                "&7Free for all com",
                "&7respawns infinitos.",
                "",
                "&6Jogadores: &f" + queueService.getPlayerCountOfGameType("FFA"),
                "",
                "&aClique para jogar!"
        )));

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }

    @AllArgsConstructor
    public static class QueuesButtonDefault extends Button {
        private String displayName;
        private Material material;
        private int durability;
        private List<String> lore;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(this.material)
                    .name(this.displayName)
                    .durability(this.durability)
                    .lore(this.lore)
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            if (clickType != ClickType.LEFT) return;

            switch (this.material) {
                case DIAMOND_SWORD:
                    new UnrankedMenu().openMenu(player);
                    break;
                case GOLD_AXE:
                    new FFAMenu().openMenu(player);
                    break;
                case GOLD_SWORD:
                    player.sendMessage(MessageConstant.IN_DEVELOPMENT);
                    break;
            }

            this.playNeutral(player);
        }
    }
}
