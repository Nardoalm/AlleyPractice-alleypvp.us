package com.kaosmc.practice.feature.kit.menu;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.kit.KitService;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.library.menu.pagination.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project Kaos
 * @since 25/06/2025
 */
@AllArgsConstructor
public class KitPotionListMenu extends PaginatedMenu {
    private final Kit kit;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&6&lPoções de " + this.kit.getDisplayName();
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        this.addGlassHeader(buttons, 15);

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        List<PotionEffect> potionEffects = this.kit.getPotionEffects();
        int index = 0;

        for (PotionEffect potionEffect : potionEffects) {
            buttons.put(index++, new KitPotionButton(this.kit, potionEffect));
        }

        return buttons;
    }

    @AllArgsConstructor
    private static class KitPotionButton extends Button {
        private final Kit kit;
        private final PotionEffect potionEffect;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.POTION)
                    .name("&6" + this.potionEffect.getType().getName())
                    .lore(
                            "&7Duração: &6" + this.potionEffect.getDuration() / 20 + " segundos",
                            "&7Amplificador: &6" + this.potionEffect.getAmplifier(),
                            "",
                            "&7Clique para remover este efeito de poção."
                    )
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            this.kit.getPotionEffects().remove(this.potionEffect);
            KaosPractice.getInstance().getService(KitService.class).saveKit(this.kit);

            LocaleService localeService = KaosPractice.getInstance().getService(LocaleService.class);
            player.sendMessage(CC.translate(localeService.getString(GlobalMessagesLocaleImpl.KIT_POTION_EFFECT_REMOVED)
                    .replace("{potion-effect}", this.potionEffect.getType().getName())
                    .replace("{kit-name}", this.kit.getName()))
            );

            new KitPotionListMenu(this.kit).openMenu(player);
        }
    }
}
