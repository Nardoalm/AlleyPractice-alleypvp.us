package us.alleypvp.practice.feature.server.menu;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.library.menu.Button;
import us.alleypvp.practice.library.menu.Menu;
import us.alleypvp.practice.feature.server.ServerService;
import us.alleypvp.practice.feature.server.menu.button.ServiceClearChatButton;
import us.alleypvp.practice.feature.server.menu.button.ServiceClearLagButton;
import us.alleypvp.practice.feature.server.menu.button.ServicePrepareRebootButton;
import us.alleypvp.practice.feature.server.menu.button.ServiceResetRebootButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
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

        if (AlleyPractice.getInstance().getService(ServerService.class).isQueueingAllowed()) {
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
