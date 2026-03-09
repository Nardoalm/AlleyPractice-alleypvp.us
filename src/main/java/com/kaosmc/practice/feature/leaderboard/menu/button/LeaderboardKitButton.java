package com.kaosmc.practice.feature.leaderboard.menu.button;

import com.kaosmc.practice.library.menu.Button;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.leaderboard.data.LeaderboardPlayerData;
import com.kaosmc.practice.feature.leaderboard.LeaderboardType;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Kaos
 * @date 3/3/2025
 */
public class LeaderboardKitButton extends Button {
    private final Kit kit;
    private final List<LeaderboardPlayerData> leaderboard;
    private final LeaderboardType type;

    public LeaderboardKitButton(Kit kit, List<LeaderboardPlayerData> leaderboard, LeaderboardType type) {
        this.kit = kit;
        this.leaderboard = leaderboard;
        this.type = type;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder builder = new ItemBuilder(this.kit.getIcon())
                .name(this.kit.getMenuTitle())
                .durability(this.kit.getDurability())
                .hideMeta();

        switch (this.type) {
            case RANKED:
                return builder.lore(generateLore("Elo")).build();
            case UNRANKED:
                return builder.lore(generateLore("Vitórias")).build();
            case FFA:
                return builder.lore(generateLore("Abates")).build();
            case WIN_STREAK:
                return builder.lore(generateLore("Vitórias")).build();
            case UNRANKED_MONTHLY:
            case TOURNAMENT:
            default:
                return this.inDevelopment();
        }
    }

    /**
     * A reusable helper method to generate the lore for any numerical leaderboard.
     *
     * @param statName The name of the statistic being displayed (e.g., "Elo", "Wins").
     * @return A list of formatted strings for the item's lore.
     */
    private List<String> generateLore(String statName) {
        if (this.leaderboard.isEmpty()) {
            return Arrays.asList("", "&7Ainda não há registros para este kit.");
        }

        List<String> lore = new ArrayList<>();
        lore.add(CC.MENU_BAR);

        List<String> topEntries = this.leaderboard.stream()
                .limit(10)
                .map(data -> {
                    int currentRank = this.leaderboard.indexOf(data) + 1;

                    String rankPrefix;
                    switch (currentRank) {
                        case 1:
                            rankPrefix = "&6&l✫" + currentRank;
                            break;
                        case 2:
                            rankPrefix = "&7&l✫" + currentRank;
                            break;
                        case 3:
                            rankPrefix = "&c&l✫" + currentRank;
                            break;
                        default:
                            rankPrefix = "&6" + currentRank + ".";
                            break;
                    }

                    return rankPrefix + " &f" + data.getName() + " &7- &f" + data.getValue() + " " + statName;
                })
                .collect(Collectors.toList());

        lore.addAll(topEntries);
        lore.add(CC.MENU_BAR);
        return lore;
    }

    /**
     * Returns a placeholder item for leaderboards that are not yet implemented.
     */
    private ItemStack inDevelopment() {
        return new ItemBuilder(Material.BARRIER)
                .name("&c&lEm Breve")
                .lore(
                        CC.MENU_BAR,
                        "&7Este ranking ainda está",
                        "&7sendo desenvolvido. Confira",
                        "&7novamente depois.",
                        CC.MENU_BAR
                )
                .build();
    }
}
