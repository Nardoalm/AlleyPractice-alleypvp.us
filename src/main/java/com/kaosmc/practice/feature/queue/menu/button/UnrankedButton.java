package com.kaosmc.practice.feature.queue.menu.button;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.PlayerUtil;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.data.types.ProfileUnrankedKitData;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.leaderboard.LeaderboardService;
import com.kaosmc.practice.feature.leaderboard.LeaderboardType;
import com.kaosmc.practice.feature.leaderboard.data.LeaderboardPlayerData;
import com.kaosmc.practice.feature.queue.Queue;
import com.kaosmc.practice.feature.server.ServerService;
import com.kaosmc.practice.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Kaos
 * @since 13/03/2025
 */
@AllArgsConstructor
public class UnrankedButton extends Button {
    protected final KaosPractice plugin = KaosPractice.getInstance();
    private final Queue queue;

    @Override
    public ItemStack getButtonItem(Player player) {
        Kit kit = this.queue.getKit();

        return new ItemBuilder(kit.getIcon())
                .name(kit.getMenuTitle())
                .durability(kit.getDurability())
                .hideMeta()
                .lore(this.getLore(kit, player))
                .hideMeta().build();
    }

    /**
     * Get the lore for the kit.
     *
     * @param kit the kit to get the lore for
     * @return the lore for the kit
     */
    private @NotNull List<String> getLore(Kit kit, Player player) {
        Profile profile = KaosPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        ProfileUnrankedKitData kitData = profile != null
                ? profile.getProfileData().getUnrankedKitData().get(kit.getName())
                : null;
        List<String> topWinStreak = this.getTopEntries(kit, LeaderboardType.WIN_STREAK);
        List<String> lore = new ArrayList<>();
        lore.add(CC.MENU_BAR);

        if (!kit.getDescription().isEmpty()) {
            Collections.addAll(lore,
                    "&7" + kit.getDescription(),
                    ""
            );
        }

        Collections.addAll(lore,
                "&6│ &rJogando: &6" + this.queue.getQueueFightCount(),
                "&6│ &rNa fila: &6" + this.queue.getTotalPlayerCount(),
                "",
                "&f&lWin Streak: &6" + (kitData != null ? kitData.getWinstreak() : 0),
                "&f&lMelhor Win Streak: &6" + (kitData != null ? kitData.getBestWinstreak() : 0),
                topWinStreak.get(0),
                topWinStreak.get(1),
                topWinStreak.get(2),
                "",
                "&aClique para jogar.",
                CC.MENU_BAR
        );

        return lore;
    }

    private @NotNull List<String> getTopEntries(Kit kit, LeaderboardType type) {
        List<LeaderboardPlayerData> entries = KaosPractice.getInstance()
                .getService(LeaderboardService.class)
                .getLeaderboardEntries(kit, type);
        List<String> topEntries = new ArrayList<>(3);

        for (int index = 0; index < 3; index++) {
            if (index < entries.size()) {
                LeaderboardPlayerData entry = entries.get(index);
                topEntries.add(" &f" + (index + 1) + ". &6" + entry.getName() + " &f- &6" + entry.getValue());
                continue;
            }

            topEntries.add(" &f" + (index + 1) + ". &6Ninguém &f- &6N/D");
        }

        return topEntries;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        if (clickType != ClickType.LEFT) return;

        ServerService serverService = KaosPractice.getInstance().getService(ServerService.class);
        if (!serverService.isQueueingAllowed()) {
            player.sendMessage(this.plugin.getService(LocaleService.class).getString(GlobalMessagesLocaleImpl.QUEUE_TEMPORARILY_DISABLED));
            player.closeInventory();
            return;
        }

        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        this.queue.addPlayer(player, this.queue.isRanked() ? profile.getProfileData().getRankedKitData().get(this.queue.getKit().getName()).getElo() : 0);
        this.playNeutral(player);

        PlayerUtil.reset(player, false, true);
        player.closeInventory();
    }
}
