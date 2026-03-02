package com.kaosmc.practice.core.profile.menu.match.button;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.constants.MessageConstant;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.time.DateFormat;
import com.kaosmc.practice.common.time.DateFormatter;
import com.kaosmc.practice.feature.arena.Arena;
import com.kaosmc.practice.feature.arena.ArenaService;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.kit.KitService;
import com.kaosmc.practice.feature.match.data.MatchData;
import com.kaosmc.practice.feature.match.data.types.MatchDataSolo;
import com.kaosmc.practice.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * @author Emmy
 * @project Kaos
 * @since 04/06/2025
 */
@AllArgsConstructor
public class MatchHistoryViewButton extends Button {
    protected final MatchData matchData;

    @Override
    public ItemStack getButtonItem(Player player) {
        if (this.matchData instanceof MatchDataSolo) {
            MatchDataSolo matchDataSolo = (MatchDataSolo) this.matchData;

            UUID winnerUUID = matchDataSolo.getWinner();
            UUID loserUUID = matchDataSolo.getLoser();

            String winnerName = Bukkit.getOfflinePlayer(winnerUUID).getName();
            String loserName = Bukkit.getOfflinePlayer(loserUUID).getName();

            DateFormatter dateFormatter = new DateFormatter(DateFormat.DATE_PLUS_TIME, matchDataSolo.getCreationTime());
            String date = dateFormatter.getDateFormat().format(dateFormatter.getDate());

            String rankedStatus = matchDataSolo.isRanked() ? "&6Ranked" : "&9Unranked";

            Kit kit = KaosPractice.getInstance().getService(KitService.class).getKit(matchDataSolo.getKit());
            Arena arena = KaosPractice.getInstance().getService(ArenaService.class).getArenaByName(matchDataSolo.getArena());

            return new ItemBuilder(Material.SKULL_ITEM)
                    .name("&a&l" + winnerName + " &7vs &c&l" + loserName + " &7(" + rankedStatus + "&7)")
                    .setSkull(winnerName)
                    .lore(
                            "&7" + date,
                            "",
                            "&6&lParticipants",
                            " &f● Winner: &a" + winnerName,
                            " &f● Loser: &c" + loserName,
                            "",
                            "&6&lMatch Details",
                            " &f● Kit: &6" + kit.getDisplayName(),
                            " &f● Arena: &6" + arena.getDisplayName(),
                            "",
                            "&aClick to view more details!"
                    )
                    .hideMeta()
                    .durability(3)
                    .build();
        } else {
            return new ItemBuilder(Material.BARRIER)
                    .name("&c&lNot implemented")
                    .lore(
                            "&fThis is not implemented for team matches yet."
                    )
                    .hideMeta()
                    .build();
        }
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        player.sendMessage(MessageConstant.IN_DEVELOPMENT);
        player.closeInventory();
    }
}