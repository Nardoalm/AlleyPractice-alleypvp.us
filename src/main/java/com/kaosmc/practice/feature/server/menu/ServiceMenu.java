package com.kaosmc.practice.feature.server.menu;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.library.menu.Menu;
import com.kaosmc.practice.feature.server.ServerService;
import com.kaosmc.practice.feature.server.menu.button.ServiceClearChatButton;
import com.kaosmc.practice.feature.server.menu.button.ServiceClearLagButton;
import com.kaosmc.practice.feature.server.menu.button.ServicePrepareRebootButton;
import com.kaosmc.practice.feature.server.menu.button.ServiceResetRebootButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Kaos
 * @since 09/03/2025
 */
public class ServiceMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&c&lService Menu";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(11, new ServiceClearChatButton());

        if (KaosPractice.getInstance().getService(ServerService.class).isQueueingAllowed()) {
            buttons.put(13, new ServicePrepareRebootButton());
        } else {
            buttons.put(13, new ServiceResetRebootButton());
        }

        buttons.put(15, new ServiceClearLagButton());

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }
}
