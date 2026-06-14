package us.alleypvp.practice.core.profile.menu.match.button;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.common.constants.MessageConstant;
import us.alleypvp.practice.common.item.ItemBuilder;
import us.alleypvp.practice.common.time.DateFormat;
import us.alleypvp.practice.common.time.DateFormatter;
import us.alleypvp.practice.feature.arena.Arena;
import us.alleypvp.practice.feature.arena.ArenaService;
import us.alleypvp.practice.feature.kit.Kit;
import us.alleypvp.practice.feature.kit.KitService;
import us.alleypvp.practice.feature.match.data.MatchData;
import us.alleypvp.practice.feature.match.data.types.MatchDataSolo;
import us.alleypvp.practice.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
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

            String rankedStatus = matchDataSolo.isRanked() ? "&bRanked" : "&9Unranked";

            Kit kit = AlleyPractice.getInstance().getService(KitService.class).getKit(matchDataSolo.getKit());
            Arena arena = AlleyPractice.getInstance().getService(ArenaService.class).getArenaByName(matchDataSolo.getArena());

            return new ItemBuilder(Material.SKULL_ITEM)
                    .name("&a&l" + winnerName + " &7vs &c&l" + loserName + " &7(" + rankedStatus + "&7)")
                    .setSkull(winnerName)
                    .lore(
                            "&7" + date,
                            "",
                            "&b&lParticipantes",
                            " &f● Vencedor: &a" + winnerName,
                            " &f● Perdedor: &c" + loserName,
                            "",
                            "&b&lDetalhes da Partida",
                            " &f● Kit: &b" + kit.getDisplayName(),
                            " &f● Arena: &b" + arena.getDisplayName(),
                            "",
                            "&aClique para ver mais detalhes!"
                    )
                    .hideMeta()
                    .durability(3)
                    .build();
        } else {
            return new ItemBuilder(Material.BARRIER)
                    .name("&c&lNão implementado")
                    .lore(
                            "&fIsso ainda não foi implementado para partidas em equipe."
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
