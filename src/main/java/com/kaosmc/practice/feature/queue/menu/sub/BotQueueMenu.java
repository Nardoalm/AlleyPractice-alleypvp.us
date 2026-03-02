package com.kaosmc.practice.feature.queue.menu.sub;

import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.library.menu.Menu;
import com.kaosmc.practice.library.menu.impl.BackButton;
import com.kaosmc.practice.feature.queue.Queue;
import com.kaosmc.practice.feature.queue.menu.QueuesMenuDefault;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Kaos
 * @date 14/09/2024 - 23:58
 */
@AllArgsConstructor
public class BotQueueMenu extends Menu {
    private final Queue queue;

    @Override
    public String getTitle(Player player) {
        return "&6&lBot Queue";
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