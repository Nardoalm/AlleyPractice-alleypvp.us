package com.kaosmc.practice.feature.title.menu;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.progress.PlayerProgress;
import com.kaosmc.practice.core.profile.progress.ProgressService;
import com.kaosmc.practice.feature.title.model.TitleRecord;
import com.kaosmc.practice.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project Kaos
 * @since 22/04/2025
 */
@AllArgsConstructor
public class TitleButton extends Button {
    private final Profile profile;
    private final TitleRecord title;

    @Override
    public ItemStack getButtonItem(Player player) {

        if (!this.profile.getProfileData().getUnlockedTitles().contains(this.title.getKit().getName())) {
            return new ItemBuilder(Material.STAINED_GLASS_PANE)
                    .name("&6&l" + this.title.getKit().getName())
                    .lore(
                            this.progress()
                    )
                    .durability(14)
                    .hideMeta()
                    .build();
        }

        return new ItemBuilder(this.title.getKit().getIcon())
                .name("&6&l" + this.title.getKit().getName())
                .lore(
                        CC.MENU_BAR,
                        " &a&lDESBLOQUEADO",
                        "",
                        "&aClique para selecionar!",
                        CC.MENU_BAR
                )
                .durability(this.title.getKit().getDurability())
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        if (!this.profile.getProfileData().getUnlockedTitles().contains(this.title.getKit().getName())) {
            player.sendMessage(CC.translate("&cPara selecionar este título, você precisa desbloqueá-lo primeiro."));
            return;
        }

        if (this.profile.getProfileData().getSelectedTitle().equals(this.title.getKit().getName())) {
            player.sendMessage(CC.translate("&cVocê já está com este título selecionado."));
            return;
        }

        this.profile.getProfileData().setSelectedTitle(this.title.getKit().getName());
        player.sendMessage(CC.translate("&aVocê selecionou o título &6" + this.title.getKit().getName() + "&a."));
    }

    /**
     * Get the progress of the title.
     *
     * @return A list of strings representing the progress.
     */
    private List<String> progress() {
        PlayerProgress progress = KaosPractice.getInstance().getService(ProgressService.class).calculateProgress(this.profile, this.title.getKit().getName());

        return Arrays.asList(
                CC.MENU_BAR,
                " &c&lBLOQUEADO",
                "",
                String.format(" &fDesbloqueie &6%s &fcom mais %d %s.",
                        progress.getNextRankName(),
                        progress.getWinsRequired(),
                        progress.getWinOrWins()
                ),
                "&f " + progress.getProgressBar(12, "■") + " &7" + progress.getProgressPercentage(),
                "",
                "&fRequer a divisão &c&l" + this.title.getRequiredDivision().getName().toUpperCase() + "&f.",
                CC.MENU_BAR
        );
    }
}
