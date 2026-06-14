package us.alleypvp.practice.feature.queue.menu.sub;

import us.alleypvp.practice.library.menu.Button;
import us.alleypvp.practice.library.menu.Menu;
import us.alleypvp.practice.library.menu.impl.BackButton;
import us.alleypvp.practice.feature.queue.Queue;
import us.alleypvp.practice.feature.queue.menu.QueuesMenuDefault;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 14/09/2024 - 23:58
 */
@AllArgsConstructor
public class BotQueueMenu extends Menu {
    private final Queue queue;

    @Override
    public String getTitle(Player player) {
        return "&b&lFila de Bot";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(new QueuesMenuDefault()));

        this.addBorder(buttons, 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}
