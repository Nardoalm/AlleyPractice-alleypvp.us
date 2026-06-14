package us.alleypvp.practice.feature.ffa.menu;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.library.menu.Button;
import us.alleypvp.practice.library.menu.Menu;
import us.alleypvp.practice.library.menu.impl.BackButton;
import us.alleypvp.practice.feature.queue.menu.QueuesMenuDefault;
import us.alleypvp.practice.feature.ffa.FFAMatch;
import us.alleypvp.practice.feature.ffa.FFAService;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 01:28
 */
@AllArgsConstructor
public class FFAMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&b&lSelecionar Fila de FFA";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(new QueuesMenuDefault()));

        for (FFAMatch match : AlleyPractice.getInstance().getService(FFAService.class).getMatches()) {
            buttons.put(match.getKit().getFfaSlot(), new FFAButton(match));
        }

        this.addBorder(buttons, 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }
}
