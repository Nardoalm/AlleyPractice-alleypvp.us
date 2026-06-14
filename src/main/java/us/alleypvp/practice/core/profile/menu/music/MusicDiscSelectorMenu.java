package us.alleypvp.practice.core.profile.menu.music;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.library.menu.Button;
import us.alleypvp.practice.library.menu.Menu;
import us.alleypvp.practice.library.menu.impl.BackButton;
import us.alleypvp.practice.feature.music.MusicService;
import us.alleypvp.practice.feature.music.MusicDisc;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.menu.music.button.MusicDiscSelectorButton;
import us.alleypvp.practice.core.profile.menu.music.button.ToggleLobbyMusicButton;
import us.alleypvp.practice.core.profile.menu.setting.PracticeSettingsMenu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project kaos-practice
 * @since 19/07/2025
 */
public class MusicDiscSelectorMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&b&lSongs";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Profile profile = AlleyPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());

        MusicService musicService = AlleyPractice.getInstance().getService(MusicService.class);
        List<MusicDisc> musicDiscs = musicService.getMusicDiscs();

        int slot = 10;
        for (MusicDisc disc : musicDiscs) {
            slot = this.skipIfSlotCrossingBorder(slot);
            buttons.put(slot, new MusicDiscSelectorButton(profile, disc));
            slot++;
        }

        this.addBorder(buttons, (short) 15, 4);

        buttons.put(4, new ToggleLobbyMusicButton());
        buttons.put(0, new BackButton(new PracticeSettingsMenu()));

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 4;
    }
}