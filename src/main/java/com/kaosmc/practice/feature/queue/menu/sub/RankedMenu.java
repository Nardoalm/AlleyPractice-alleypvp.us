package com.kaosmc.practice.feature.queue.menu.sub;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.library.menu.Menu;
import com.kaosmc.practice.feature.queue.QueueService;
import com.kaosmc.practice.feature.queue.Queue;
import com.kaosmc.practice.feature.queue.menu.button.RankedButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Kaos
 * @date 23/05/2024 - 01:28
 */
public class RankedMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&6&lFila Solo Ranked";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int slot = 10;

        for (Queue queue : KaosPractice.getInstance().getService(QueueService.class).getQueues()) {
            if (queue.isRanked()) {
                slot = this.skipIfSlotCrossingBorder(slot);
                buttons.put(slot++, new RankedButton(queue));
            }
        }

        this.addBorder(buttons, 15, 5);

        return buttons;
    }


    @Override
    public int getSize() {
        return 9 * 5;
    }
}
