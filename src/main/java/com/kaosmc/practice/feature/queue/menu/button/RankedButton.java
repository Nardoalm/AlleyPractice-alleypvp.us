package com.kaosmc.practice.feature.queue.menu.button;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.PlayerUtil;
import com.kaosmc.practice.common.item.ItemBuilder;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.feature.hotbar.HotbarService;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Kaos
 * @since 13/03/2025
 */
@AllArgsConstructor
public class RankedButton extends Button {
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
        List<String> topRanked = this.getTopEntries(kit, LeaderboardType.RANKED);
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
                "&6│ &rNa fila: &6" + this.queue.getProfiles().size(),
                "",
                "&f&lSeu ELO: &6" + KaosPractice.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId()).getProfileData().getRankedKitData().get(kit.getName()).getElo(),
                topRanked.get(0),
                topRanked.get(1),
                topRanked.get(2),
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
            player.sendMessage(KaosPractice.getInstance().getService(LocaleService.class).getString(GlobalMessagesLocaleImpl.QUEUE_TEMPORARILY_DISABLED));
            player.closeInventory();
            return;
        }

        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getProfileData().isRankedBanned()) {
            player.closeInventory();
            Arrays.asList(
                    "",
                    "&c&lBANIMENTO RANKED",
                    "&cVocê está banido das filas ranked no momento.",
                    "&7Você pode recorrer em &6&ndiscord.gg/kaos-practice&7.",
                    ""
            ).forEach(line -> player.sendMessage(CC.translate(line)));
            return;
        }

        this.queue.addPlayer(player, this.queue.isRanked() ? profile.getProfileData().getRankedKitData().get(queue.getKit().getName()).getElo() : 0);
        PlayerUtil.reset(player, false, true);
        player.closeInventory();
        this.playNeutral(player);
        KaosPractice.getInstance().getService(HotbarService.class).applyHotbarItems(player);
    }
}
