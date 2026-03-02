package com.kaosmc.practice.feature.party.menu.event.impl;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.library.menu.Menu;
import com.kaosmc.practice.feature.queue.QueueService;
import com.kaosmc.practice.feature.queue.Queue;
import com.kaosmc.practice.feature.party.menu.event.impl.button.PartyEventSplitButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Kaos
 * @date 08/10/2024 - 18:38
 */
public class PartyEventSplitMenu extends Menu {
    protected final KaosPractice plugin = KaosPractice.getInstance();

    @Override
    public String getTitle(Player player) {
        return "&6&lSelect a kit";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        int slot = 10;
        for (Queue queue : KaosPractice.getInstance().getService(QueueService.class).getQueues()) {
            if (!queue.isRanked() && !queue.isDuos() && queue.getKit().isEnabled()) {
                slot = this.skipIfSlotCrossingBorder(slot);
                buttons.put(slot++, new PartyEventSplitButton(queue.getKit()));
            }
        }

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 6;
    }
}