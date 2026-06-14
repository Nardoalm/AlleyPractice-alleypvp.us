package us.alleypvp.practice.feature.queue.menu.sub;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.library.menu.Button;
import us.alleypvp.practice.library.menu.Menu;
import us.alleypvp.practice.library.menu.impl.BackButton;
import us.alleypvp.practice.feature.kit.KitCategory;
import us.alleypvp.practice.feature.queue.QueueService;
import us.alleypvp.practice.feature.queue.Queue;
import us.alleypvp.practice.feature.queue.QueueType;
import us.alleypvp.practice.feature.queue.menu.QueuesMenuDefault;
import us.alleypvp.practice.feature.queue.menu.button.UnrankedButton;
import us.alleypvp.practice.feature.queue.menu.extra.button.QueueModeSwitcherButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 10:28
 */
public class UnrankedMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&b&lFila Unranked";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(new QueuesMenuDefault()));

        int slot = 10;
        for (Queue queue : AlleyPractice.getInstance().getService(QueueService.class).getQueues()) {
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
