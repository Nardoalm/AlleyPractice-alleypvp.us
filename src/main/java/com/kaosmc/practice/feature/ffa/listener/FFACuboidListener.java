package com.kaosmc.practice.feature.ffa.listener;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.geom.Cuboid;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.enums.ProfileState;
import com.kaosmc.practice.feature.combat.CombatService;
import com.kaosmc.practice.feature.ffa.FFAService;
import com.kaosmc.practice.feature.ffa.FFAState;
import com.kaosmc.practice.feature.ffa.spawn.FFASpawnService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Emmy
 * @project Kaos
 * @since 25/01/2025
 */
public class FFACuboidListener implements Listener {
    private final Map<UUID, Boolean> playerStates = new HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Cuboid cuboid = KaosPractice.getInstance().getService(FFASpawnService.class).getCuboid();
        if (cuboid == null) {
            return;
        }

        Player player = event.getPlayer();
        if (event.getFrom().getBlock().equals(event.getTo().getBlock())) {
            return;
        }

        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile == null || profile.getState() != ProfileState.FFA) {
            return;
        }

        FFAService ffaService = KaosPractice.getInstance().getService(FFAService.class);
        CombatService combatService = KaosPractice.getInstance().getService(CombatService.class);
        LocaleService localeService = KaosPractice.getInstance().getService(LocaleService.class);

        if (ffaService.getFFAMatch(player) == null || !ffaService.getMatchByPlayer(player).isPresent()) {
            return;
        }

        UUID playerId = player.getUniqueId();

        boolean isInCuboid = cuboid.isIn(player);
        boolean wasInCuboid = this.playerStates.getOrDefault(playerId, true);

        if (isInCuboid != wasInCuboid) {
            if (isInCuboid) {
                if (combatService.isPlayerInCombat(playerId)) return;
                player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.FFA_SPAWN_ENTERED));
                ffaService.getMatchByPlayer(player).ifPresent(match -> match.getGameFFAPlayer(player).setState(FFAState.SPAWN));
            } else {
                player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.FFA_SPAWN_LEFT));
                ffaService.getMatchByPlayer(player).ifPresent(match -> match.getGameFFAPlayer(player).setState(FFAState.FIGHTING));
            }

            this.playerStates.put(playerId, isInCuboid);
        }
    }
}