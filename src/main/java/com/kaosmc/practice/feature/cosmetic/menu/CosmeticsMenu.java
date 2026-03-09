package com.kaosmc.practice.feature.cosmetic.menu;

import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.library.menu.Menu;
import com.kaosmc.practice.library.menu.impl.BackButton;
import com.kaosmc.practice.feature.cosmetic.model.CosmeticType;
import com.kaosmc.practice.feature.cosmetic.menu.button.CosmeticCategoryButton;
import com.kaosmc.practice.core.profile.menu.setting.PracticeSettingsMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Kaos
 * @date 6/1/2024
 */
@AllArgsConstructor
public class CosmeticsMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&6&lCosméticos";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(new PracticeSettingsMenu()));
        buttons.put(10, new CosmeticCategoryButton(CosmeticType.KILL_EFFECT, Material.DIAMOND_SWORD));
        buttons.put(11, new CosmeticCategoryButton(CosmeticType.SOUND_EFFECT, Material.NOTE_BLOCK));
        buttons.put(12, new CosmeticCategoryButton(CosmeticType.CLOAK, Material.BLAZE_POWDER));
        buttons.put(13, new CosmeticCategoryButton(CosmeticType.SUIT, Material.GOLD_CHESTPLATE));
        buttons.put(14, new CosmeticCategoryButton(CosmeticType.PROJECTILE_TRAIL, Material.ARROW));
        buttons.put(15, new CosmeticCategoryButton(CosmeticType.KILL_MESSAGE, Material.BOOK_AND_QUILL));

        this.addBorder(buttons, 15, 3);

        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }
}
