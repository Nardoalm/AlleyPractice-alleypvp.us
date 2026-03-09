package com.kaosmc.practice.core.profile.menu.statistic.button;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.adapter.core.CoreAdapter;
import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.data.types.ProfileFFAData;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.common.text.LevelBadgeUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Kaos
 * @date 5/26/2024
 */
@RequiredArgsConstructor
public class GlobalStatButton extends Button {
    private final OfflinePlayer target;

    /**
     * Gets the item to display in the menu.
     *
     * @param player the player viewing the menu
     * @return the item to display
     */
    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = KaosPractice.getInstance().getService(ProfileService.class).getProfile(target.getUniqueId());
        if (profile == null || profile.getProfileData() == null) {
            return new ItemBuilder(Material.BARRIER)
                    .name("&cDados indisponiveis")
                    .lore("&7Nao foi possivel carregar os dados do jogador.")
                    .build();
        }

        CoreAdapter coreAdapter = KaosPractice.getInstance().getService(CoreAdapter.class);

        int ffaKills = profile.getProfileData().getFfaData().values().stream()
                .mapToInt(ProfileFFAData::getKills)
                .sum();
        int ffaDeaths = profile.getProfileData().getFfaData().values().stream()
                .mapToInt(ProfileFFAData::getDeaths)
                .sum();

        String levelDisplay = LevelBadgeUtil.getBadge(profile.getProfileData().getExperience());
        String rankDisplay = "&7N/D";
        Player targetPlayer = target.getPlayer();
        if (coreAdapter != null && coreAdapter.getCore() != null && targetPlayer != null) {
            rankDisplay = String.valueOf(coreAdapter.getCore().getRankColor(targetPlayer)) + coreAdapter.getCore().getRankName(targetPlayer);
        }

        return new ItemBuilder(Material.SKULL_ITEM)
                .setSkull(target.getName())
                .name("&6&l" + target.getName() + " &r&7┃ &fEstatísticas")
                .lore(
                        CC.MENU_BAR,
                        "&7Exibindo dados globais.",
                        "",
                        "&6&lUnranked",
                        "&6│ &fVitórias: &6" + profile.getProfileData().getUnrankedWins(),
                        "&6│ &fDerrotas: &6" + profile.getProfileData().getUnrankedLosses(),
                        "",
                        "&6&lRanked",
                        "&6│ &fVitórias: &6" + profile.getProfileData().getRankedWins(),
                        "&6│ &fDerrotas: &6" + profile.getProfileData().getRankedLosses(),
                        "&6│ &fElo: &6" + profile.getProfileData().getElo(),
                        "",
                        "&6&lFFA",
                        "&6│ &fKills: &6" + ffaKills,
                        "&6│ &fDeaths: &6" + ffaDeaths,
                        "",
                        "&6&lConta",
                        "&6│ &fPosicao: &6" + rankDisplay,
                        "&6│ &fMoedas: &6$" + profile.getProfileData().getCoins(),
                        "&6│ &fNível: &6" + levelDisplay,
                        CC.MENU_BAR

                )
                .build();
    }
}
