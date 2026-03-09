package com.kaosmc.practice.core.profile.menu.shop;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.library.menu.Menu;
import com.kaosmc.practice.library.menu.impl.BackButton;
import com.kaosmc.practice.feature.cosmetic.model.CosmeticType;
import com.kaosmc.practice.feature.cosmetic.internal.repository.BaseCosmeticRepository;
import com.kaosmc.practice.feature.cosmetic.CosmeticService;
import com.kaosmc.practice.core.profile.menu.shop.button.ShopItemButton;
import com.kaosmc.practice.common.text.StringUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Kaos
 * @date 6/23/2025
 */
@RequiredArgsConstructor
public class ShopCategoryMenu extends Menu {

    private final CosmeticType cosmeticType;

    @Override
    public String getTitle(Player player) {
        return "&6&lLoja - " + StringUtil.formatEnumName(cosmeticType) + "s";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(0, new BackButton(new ShopMenu()));

        BaseCosmeticRepository<?> repository = KaosPractice.getInstance().getService(CosmeticService.class).getRepository(cosmeticType);
        if (repository != null) {
            repository.getCosmetics().stream()
                    .filter(cosmetic -> cosmetic.getIcon() != null && cosmetic.getPrice() > 0)
                    .forEach(cosmetic -> buttons.put(cosmetic.getSlot(), new ShopItemButton(cosmetic)));
        }

        this.addBorder(buttons, 15, 5);

        return buttons;
    }
}
