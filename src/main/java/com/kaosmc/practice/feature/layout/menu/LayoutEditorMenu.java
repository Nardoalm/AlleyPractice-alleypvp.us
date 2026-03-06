package com.kaosmc.practice.feature.layout.menu;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.InventoryUtil;
import com.kaosmc.practice.feature.layout.menu.button.editor.*;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.library.menu.Menu;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.layout.data.LayoutData;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.enums.ProfileState;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService != null ? profileService.getProfile(player.getUniqueId()) : null;
        if (profile != null) {
            profile.setState(ProfileState.EDITING);
        }

        if (this.layout != null && this.layout.getItems() != null) {
            player.getInventory().setContents(InventoryUtil.cloneItemStackArray(this.layout.getItems()));
        } else if (this.kit != null && this.kit.getItems() != null) {
            player.getInventory().setContents(InventoryUtil.cloneItemStackArray(this.kit.getItems()));
        } else {
            player.getInventory().clear();
        }
    }

    @Override
    public void onClose(Player player) {
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService != null ? profileService.getProfile(player.getUniqueId()) : null;
        if (profile != null) {
            profile.setState(ProfileState.LOBBY);
        }
        super.onClose(player);
    }

    @Override
    public String getTitle(Player player) {
        String layoutName = this.layout != null && this.layout.getDisplayName() != null
                ? this.layout.getDisplayName()
                : "Layout";
        return "&6&lEditing " + layoutName;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService != null ? profileService.getProfile(player.getUniqueId()) : null;

        buttons.put(11, new LayoutSaveButton(this.kit, this.layout));
        buttons.put(13, new LayoutResetItemsButton(this.kit));
        buttons.put(15, new LayoutCancelButton());

        /*
         * If kit isn't the first/default one stored in profile, add the following buttons:
         */
        LayoutData firstLayout = this.getFirstLayout(profile);
        if (firstLayout != null
                && this.layout != null
                && this.layout.getName() != null
                && !this.layout.getName().equals(firstLayout.getName())) {
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

    private LayoutData getFirstLayout(Profile profile) {
        if (profile == null
                || profile.getProfileData() == null
                || profile.getProfileData().getLayoutData() == null
                || profile.getProfileData().getLayoutData().getLayouts() == null
                || this.kit == null) {
            return null;
        }

        List<LayoutData> layouts = profile.getProfileData().getLayoutData().getLayouts().get(this.kit.getName());
        if (layouts == null || layouts.isEmpty()) {
            return null;
        }

        return layouts.stream().filter(Objects::nonNull).findFirst().orElse(null);
    }
}
