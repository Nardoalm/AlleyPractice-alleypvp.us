package com.kaosmc.practice.feature.arena;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.arena.internal.types.StandAloneArena;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.kit.KitService;
import com.kaosmc.practice.feature.kit.setting.types.mode.KitSettingBridges;
import com.kaosmc.practice.feature.kit.setting.types.mode.KitSettingRounds;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project kaos-practice
 * @since 07/09/2025
 */
public class ArenaValidator {
    /**
     * Validates if an arena is fully configured before enabling or disabling it.
     * Sends appropriate messages to the player if validation fails.
     *
     * @param player the player attempting to enable/disable the arena
     * @param arena  the arena to be validated
     * @return true if the arena is fully configured, false otherwise
     */
    public boolean isEligible(Player player, Arena arena) {
        LocaleService localeService = KaosPractice.getInstance().getService(LocaleService.class);

        if (arena.getMinimum() == null || arena.getMaximum() == null) {
            player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.ARENA_NO_SELECTION));
            return false;
        }

        if (arena.getPos1() == null || arena.getPos2() == null) {
            player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.ARENA_SPAWN_NOT_SET));
            return false;
        }

        if (arena.getCenter() == null) {
            player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.ARENA_CENTER_NOT_SET));
            return false;
        }

        if (arena.getKits().isEmpty()) {
            player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.ARENA_MUST_ADD_KIT));
            return false;
        }

        KitService kitService = KaosPractice.getInstance().getService(KitService.class);
        for (String kitName : arena.getKits()) {
            Kit kit = kitService.getKit(kitName);
            if (kit == null) {
                player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.ARENA_ASSIGNED_KIT_NULL).replace("{kit-name}", kitName));
                return false;
            }

            if (arena.getType() == ArenaType.STANDALONE) {
                StandAloneArena standAloneArena = (StandAloneArena) arena;
                if ((kit.isSettingEnabled(KitSettingRounds.class) || kit.isSettingEnabled(KitSettingBridges.class)) && (standAloneArena.getTeam1Portal() == null || standAloneArena.getTeam2Portal() == null)) {
                    player.sendMessage(localeService.getString(GlobalMessagesLocaleImpl.ARENA_STANDALONE_PORTALS_NOT_SET));
                    return false;
                }
            }
        }

        return true;
    }
}