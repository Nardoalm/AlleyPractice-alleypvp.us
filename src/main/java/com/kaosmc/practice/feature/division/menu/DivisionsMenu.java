package com.kaosmc.practice.feature.division.menu;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.library.menu.Menu;
import com.kaosmc.practice.feature.division.Division;
import com.kaosmc.practice.feature.division.DivisionService;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Kaos
 * @since 25/01/2025
 */
public class DivisionsMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&6&lDivisões";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        int slot = 10;
        for (Division division : KaosPractice.getInstance().getService(DivisionService.class).getDivisions()) {
            buttons.put(slot++, new DivisionButton(division));
            if (slot == 17 || slot == 26 || slot == 35 || slot == 44 || slot == 53) {
                slot += 2;
            }
        }

        this.addBorder(buttons, 15, 6);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 6;
    }
}
