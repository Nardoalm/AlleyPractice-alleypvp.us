package com.kaosmc.practice.feature.queue.menu.sub;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.library.menu.Menu;
import com.kaosmc.practice.library.menu.impl.BackButton;
import com.kaosmc.practice.feature.kit.KitCategory;
import com.kaosmc.practice.feature.queue.QueueService;
import com.kaosmc.practice.feature.queue.Queue;
import com.kaosmc.practice.feature.queue.QueueType;
import com.kaosmc.practice.feature.queue.menu.QueuesMenuDefault;
import com.kaosmc.practice.feature.queue.menu.button.UnrankedButton;
import com.kaosmc.practice.feature.queue.menu.extra.button.QueueModeSwitcherButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Kaos
 * @date 23/05/2024 - 10:28
 */
public class UnrankedMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&6&lUnranked Queue";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(new QueuesMenuDefault()));

        int slot = 10;
        for (Queue queue : KaosPractice.getInstance().getService(QueueService.class).getQueues()) {
            if (!queue.isRanked() && queue.getKit().getCategory() == KitCategory.NORMAL) {
                slot = this.skipIfSlotCrossingBorder(slot);
                buttons.put(slot++, new UnrankedButton(queue));
            }
        }

        buttons.put(40, new QueueModeSwitcherButton(QueueType.UNRANKED, KitCategory.EXTRA));

        this.addBorder(buttons, 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}