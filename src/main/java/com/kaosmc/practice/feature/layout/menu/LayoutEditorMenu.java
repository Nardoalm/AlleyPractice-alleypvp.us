package com.kaosmc.practice.feature.layout.menu;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.feature.layout.menu.button.editor.*;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.library.menu.Menu;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.layout.data.LayoutData;
import dev.revere.alley.feature.layout.menu.button.editor.*;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.enums.ProfileState;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Kaos
 * @since 03/05/2025
 */
@AllArgsConstructor
public class LayoutEditorMenu extends Menu {
    protected final KaosPractice plugin = KaosPractice.getInstance();
    private final Kit kit;
    private final LayoutData layout;

    @Override
    public void onOpen(Player player) {
        KaosPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId()).setState(ProfileState.EDITING);
        player.getInventory().setContents(this.layout.getItems());
    }

    @Override
    public void onClose(Player player) {
        KaosPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId()).setState(ProfileState.LOBBY);
        super.onClose(player);
    }

    @Override
    public String getTitle(Player player) {
        return "&6&lEditing " + this.layout.getDisplayName();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        buttons.put(11, new LayoutSaveButton(this.kit, this.layout));
        buttons.put(13, new LayoutResetItemsButton(this.kit));
        buttons.put(15, new LayoutCancelButton());

        /*
         * If kit isn't the first/default one stored in profile, add the following buttons:
         */
        if (!this.layout.getName().equals(profile.getProfileData().getLayoutData().getLayouts().get(this.kit.getName()).get(0).getName())) {
            buttons.put(21, new LayoutDeleteButton(this.layout));
            buttons.put(23, new LayoutRenameButton(this.layout));
        }

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        //if kit isn't the first/default one, return 9*4 because of the delete and rename buttons
        return 9 * 3;
    }

    @Override
    public boolean isUpdateAfterClick() {
        return false;
    }
}