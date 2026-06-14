package us.alleypvp.practice.feature.queue.menu.button;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.common.PlayerUtil;
import us.alleypvp.practice.common.item.ItemBuilder;
import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.core.locale.LocaleService;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.data.types.ProfileUnrankedKitData;
import us.alleypvp.practice.feature.kit.Kit;
import us.alleypvp.practice.feature.leaderboard.LeaderboardService;
import us.alleypvp.practice.feature.leaderboard.LeaderboardType;
import us.alleypvp.practice.feature.leaderboard.data.LeaderboardPlayerData;
import us.alleypvp.practice.feature.queue.Queue;
import us.alleypvp.practice.feature.server.ServerService;
import us.alleypvp.practice.library.menu.Button;
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
 * @project Alley
 * @since 13/03/2025
 */
@AllArgsConstructor
public class UnrankedButton extends Button {
    protected final AlleyPractice plugin = AlleyPractice.getInstance();
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
        Profile profile = AlleyPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
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
                "&b│ &rPlaying: &b" + this.queue.getQueueFightCount(),
                "&b│ &rIn queue: &b" + this.queue.getTotalPlayerCount(),
                "",
                "&f&lWinstreak: &b" + (kitData != null ? kitData.getWinstreak() : 0),
                "&f&lBest Winstreak: &b" + (kitData != null ? kitData.getBestWinstreak() : 0),
                topWinStreak.get(0),
                topWinStreak.get(1),
                topWinStreak.get(2),
                "",
                "&aClick to play.",
                CC.MENU_BAR
        );

        return lore;
    }

    private @NotNull List<String> getTopEntries(Kit kit, LeaderboardType type) {
        List<LeaderboardPlayerData> entries = AlleyPractice.getInstance()
                .getService(LeaderboardService.class)
                .getLeaderboardEntries(kit, type);
        List<String> topEntries = new ArrayList<>(3);

        for (int index = 0; index < 3; index++) {
            if (index < entries.size()) {
                LeaderboardPlayerData entry = entries.get(index);
                topEntries.add(" &f" + (index + 1) + ". &b" + entry.getName() + " &f- &b" + entry.getValue());
                continue;
            }

            topEntries.add(" &f" + (index + 1) + ". &bNobody &f- &bN/D");
        }

        return topEntries;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        if (clickType != ClickType.LEFT) return;

        ServerService serverService = AlleyPractice.getInstance().getService(ServerService.class);
        if (!serverService.isQueueingAllowed()) {
            player.sendMessage(this.plugin.getService(LocaleService.class).getString(GlobalMessagesLocaleImpl.QUEUE_TEMPORARILY_DISABLED));
            player.closeInventory();
            return;
        }

        ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        this.queue.addPlayer(player, this.queue.isRanked() ? profile.getProfileData().getRankedKitData().get(this.queue.getKit().getName()).getElo() : 0);
        this.playNeutral(player);

        PlayerUtil.reset(player, false, true);
        player.closeInventory();
    }
}
