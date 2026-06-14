package us.alleypvp.practice.feature.level.menu;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.library.menu.Button;
import us.alleypvp.practice.library.menu.pagination.PaginatedMenu;
import us.alleypvp.practice.feature.level.LevelService;
import us.alleypvp.practice.feature.level.data.LevelData;
import us.alleypvp.practice.core.profile.Profile;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @since 22/04/2025
 */
@AllArgsConstructor
public class LevelMenu extends PaginatedMenu {
    private final Profile profile;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&b&lLevels";
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

        int slot = 0;

        for (LevelData level : AlleyPractice.getInstance().getService(LevelService.class).getLevels()) {
            slot = this.validateSlot(slot);
            buttons.put(slot++, new LevelButton(this.profile, level));
        }

        this.addGlassToAvoidedSlots(buttons);

        return buttons;
    }
}