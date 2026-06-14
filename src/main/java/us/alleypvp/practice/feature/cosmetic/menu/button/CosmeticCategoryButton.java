package us.alleypvp.practice.feature.cosmetic.menu.button;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.library.menu.Button;
import us.alleypvp.practice.feature.cosmetic.model.CosmeticType;
import us.alleypvp.practice.feature.cosmetic.menu.CosmeticTypeMenu;
import us.alleypvp.practice.feature.cosmetic.internal.repository.BaseCosmeticRepository;
import us.alleypvp.practice.feature.cosmetic.CosmeticService;
import us.alleypvp.practice.common.item.ItemBuilder;
import us.alleypvp.practice.common.text.StringUtil;
import us.alleypvp.practice.common.text.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 6/23/2025
 */
@RequiredArgsConstructor
public class CosmeticCategoryButton extends Button {

    private final CosmeticType cosmeticType;
    private final Material icon;

    @Override
    public ItemStack getButtonItem(Player player) {
        String friendlyName = StringUtil.formatEnumName(cosmeticType);

        int totalCount = 0;
        int ownedCount = 0;

        BaseCosmeticRepository<?> repository = AlleyPractice.getInstance().getService(CosmeticService.class).getRepository(cosmeticType);
        if (repository != null) {
            totalCount = repository.getCosmetics().size();
            ownedCount = (int) repository.getCosmetics().stream()
                    .filter(cosmetic -> player.hasPermission(cosmetic.getPermission()))
                    .count();
        }

        int percentage = (totalCount == 0) ? 0 : (int) (((double) ownedCount / totalCount) * 100);

        List<String> lore = new ArrayList<>();
        lore.add(CC.MENU_BAR);
        lore.add("&7" + cosmeticType.getDescription());
        lore.add("");
        lore.add(String.format("&b│ &fDesbloqueados: &b%d/%d &7(%d%%)", ownedCount, totalCount, percentage));
        lore.add("");
        lore.add("&aClique para visualizar.");
        lore.add(CC.MENU_BAR);

        return new ItemBuilder(this.icon)
                .name("&b&l" + friendlyName + "s")
                .lore(lore)
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        this.playNeutral(player);
        new CosmeticTypeMenu(cosmeticType).openMenu(player);
    }
}
