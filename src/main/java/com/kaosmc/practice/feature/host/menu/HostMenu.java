package com.kaosmc.practice.feature.host.menu;

import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.feature.event.menu.EventMenu;
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
 * @date 08/06/2024 - 21:19
 */
public class HostMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&6&lHospedar Evento";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(11, new HostButton("&6&lEvento", new ItemStack(Material.EMPTY_MAP), Arrays.asList(
                "&fHospede um evento com",
                "&fimplementações diferentes.",
                "",
                " &f◆ &6Tipos: &7Sumo, Brackets, Gulag, LMS",
                " &f◆ &7Knockout, OITC, Parkour, Dropper",
                " &f◆ &7SkyWars, Spleef, Stoplight, 4Corners",
                " &f◆ &7Thimble e TNT Tag",
                "",
                "&aClique para hospedar!"
        )));

        buttons.put(15, new HostButton("&6&lTorneio", new ItemStack(Material.BOW), Arrays.asList(
                "&fHospede um torneio para",
                "&fcompetir em uma série",
                "&fde duelos até vencer.",
                "",
                "&aClique para hospedar!"
        )));

        this.addGlass(buttons, 15);
        return buttons;
    }

    @AllArgsConstructor
    public static class HostButton extends Button {
        private String displayName;
        private ItemStack itemStack;
        private List<String> lore;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(this.itemStack)
                    .name(this.displayName)
                    .lore(this.lore)
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) {
                return;
            }

            switch (this.itemStack.getType()) {
                case BOW:
                    // Open the tournament menu
                    break;
                case EMPTY_MAP:
                    new EventMenu().openMenu(player);
                    break;
            }

            this.playNeutral(player);
        }
    }


    @Override
    public int getSize() {
        return 9 * 3;
    }
}
