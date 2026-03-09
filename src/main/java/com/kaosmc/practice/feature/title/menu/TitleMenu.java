package com.kaosmc.practice.feature.title.menu;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.library.menu.pagination.PaginatedMenu;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.title.TitleService;
import com.kaosmc.practice.feature.title.model.TitleRecord;
import com.kaosmc.practice.core.profile.Profile;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Kaos
 * @since 22/04/2025
 */
@AllArgsConstructor
public class TitleMenu extends PaginatedMenu {
    private final Profile profile;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&6&lSeus Títulos";
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

        Map<Kit, TitleRecord> titleMap = KaosPractice.getInstance().getService(TitleService.class).getTitles();
        for (TitleRecord title : titleMap.values()) {
            slot = this.validateSlot(slot);
            buttons.put(slot++, new TitleButton(this.profile, title));
        }

        this.addGlassToAvoidedSlots(buttons);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}
