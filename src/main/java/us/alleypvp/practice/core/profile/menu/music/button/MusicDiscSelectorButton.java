package us.alleypvp.practice.core.profile.menu.music.button;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.common.item.ItemBuilder;
import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.common.text.LoreHelper;
import us.alleypvp.practice.core.locale.LocaleService;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.data.types.ProfileMusicData;
import us.alleypvp.practice.feature.music.MusicDisc;
import us.alleypvp.practice.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project kaos-practice
 * @since 19/07/2025
 */
@AllArgsConstructor
public class MusicDiscSelectorButton extends Button {
    private final Profile profile;
    private final MusicDisc disc;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.disc.getMaterial())
                .name("&b&l" + this.disc.getTitle())
                .lore(
                        CC.MENU_BAR,
                        LoreHelper.displayToggled(this.profile.getProfileData().getMusicData().isDiscSelected(this.disc.name())),
                        "",
                        "&aClique para alternar.",
                        CC.MENU_BAR
                )
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        ProfileMusicData musicData = this.profile.getProfileData().getMusicData();
        LocaleService localeService = AlleyPractice.getInstance().getService(LocaleService.class);

        if (musicData.getSelectedDiscs().isEmpty()) {
            player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.ERROR_MUST_SELECT_MUSIC));
            return;
        }

        if (musicData.isDiscSelected(this.disc.name())) {
            musicData.removeDisc(this.disc.name());
            player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.MUSIC_DISC_DESELECTED).replace("{disc}", this.disc.getTitle()));
        } else {
            musicData.addDisc(this.disc.name());
            player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.MUSIC_DISC_SELECTED).replace("{disc}", this.disc.getTitle()));
        }

        this.playNeutral(player);
    }
}
