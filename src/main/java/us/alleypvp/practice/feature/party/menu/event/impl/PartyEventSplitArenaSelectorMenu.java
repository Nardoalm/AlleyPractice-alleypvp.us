package us.alleypvp.practice.feature.party.menu.event.impl;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.library.menu.Button;
import us.alleypvp.practice.library.menu.pagination.PaginatedMenu;
import us.alleypvp.practice.feature.arena.Arena;
import us.alleypvp.practice.feature.arena.ArenaService;
import us.alleypvp.practice.feature.kit.Kit;
import us.alleypvp.practice.feature.party.menu.event.impl.button.PartyEventSplitArenaSelectorButton;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 09/11/2024 - 09:37
 */
@AllArgsConstructor
public class PartyEventSplitArenaSelectorMenu extends PaginatedMenu {
    private Kit kit;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&b&lSelect an arena";
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        this.addGlassHeader(buttons, 15);

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Arena arena : AlleyPractice.getInstance().getService(ArenaService.class).getArenas()) {
            if (arena.getKits().contains(kit.getName()) && arena.isEnabled()) {
                buttons.put(buttons.size(), new PartyEventSplitArenaSelectorButton(kit, arena));
            }
        }

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 6;
    }
}