package com.kaosmc.practice.feature.match.task.other;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.reflect.ReflectionService;
import com.kaosmc.practice.common.reflect.internal.types.TitleReflectionServiceImpl;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.VisualsLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.feature.arena.internal.types.StandAloneArena;
import com.kaosmc.practice.feature.match.Match;
import com.kaosmc.practice.feature.match.MatchState;
import com.kaosmc.practice.feature.match.model.internal.MatchGamePlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Remi
 * @project kaos-practice
 * @date 22/07/2025
 */
public class MatchCampProtectionTask extends BukkitRunnable {
    private final Player player;
    private int ticks;

    private static final int INITIAL_GRACE_PERIOD_SECONDS = 3;
    private static final int COUNTDOWN_DURATION_SECONDS = 3;

    /**
     * Constructor for the MatchCampProtectionTask class.
     *
     * @param player The player to apply camp protection to.
     */
    public MatchCampProtectionTask(Player player) {
        this.player = player;
        this.ticks = 0;
    }

    @Override
    public void run() {
        if (this.player == null || !this.player.isOnline()) {
            this.cancel();
            return;
        }

        Profile profile = KaosPractice.getInstance().getService(ProfileService.class).getProfile(this.player.getUniqueId());
        Match match = profile.getMatch();
        if (match == null) {
            this.cancel();
            return;
        }

        if (match.getState() == MatchState.ENDING_MATCH) {
            this.cancel();
            return;
        }

        StandAloneArena matchArena = (StandAloneArena) match.getArena();
        int CAMP_Y_LEVEL = matchArena.getHeightLimit();

        MatchGamePlayer gamePlayer = match.getGamePlayer(player);
        if (this.player.getLocation().getY() <= CAMP_Y_LEVEL + 3
                || gamePlayer.isDead()
                || gamePlayer.isEliminated()
                || this.player.getGameMode() == GameMode.CREATIVE
                || this.player.getGameMode() == GameMode.SPECTATOR) {
            ticks = 0;
            return;
        }

        this.ticks++;
        int damageStartPeriod = INITIAL_GRACE_PERIOD_SECONDS + COUNTDOWN_DURATION_SECONDS;

        TitleReflectionServiceImpl titleReflectionService = KaosPractice.getInstance().getService(ReflectionService.class).getReflectionService(TitleReflectionServiceImpl.class);
        LocaleService localeService = KaosPractice.getInstance().getService(LocaleService.class);


        if (ticks <= damageStartPeriod) {
            int countdownValue = damageStartPeriod - ticks + 1;

            if (localeService.getBoolean(VisualsLocaleImpl.TITLE_CAMP_PROTECTION_ENABLED_BOOLEAN)) {
                String header = localeService.getString(VisualsLocaleImpl.TITLE_CAMP_PROTECTION_HEADER);
                String footer = localeService.getString(VisualsLocaleImpl.TITLE_CAMP_PROTECTION_FOOTER).replace("{seconds}", String.valueOf(countdownValue));

                int fadeIn = localeService.getInt(VisualsLocaleImpl.TITLE_CAMP_PROTECTION_FADE_IN);
                int stay = localeService.getInt(VisualsLocaleImpl.TITLE_CAMP_PROTECTION_STAY);
                int fadeOut = localeService.getInt(VisualsLocaleImpl.TITLE_CAMP_PROTECTION_FADEOUT);

                titleReflectionService.sendTitle(this.player, header, footer, fadeIn, stay, fadeOut);
            }
        } else {
            this.player.damage(4.0);

            if (localeService.getBoolean(VisualsLocaleImpl.TITLE_CAMP_PROTECTION_TAKING_DAMAGE_ENABLED_BOOLEAN)) {
                String header = localeService.getString(VisualsLocaleImpl.TITLE_CAMP_PROTECTION_TAKING_DAMAGE_HEADER);
                String footer = localeService.getString(VisualsLocaleImpl.TITLE_CAMP_PROTECTION_TAKING_DAMAGE_FOOTER);

                int fadeIn = localeService.getInt(VisualsLocaleImpl.TITLE_CAMP_PROTECTION_TAKING_DAMAGE_FADE_IN);
                int stay = localeService.getInt(VisualsLocaleImpl.TITLE_CAMP_PROTECTION_TAKING_DAMAGE_STAY);
                int fadeOut = localeService.getInt(VisualsLocaleImpl.TITLE_CAMP_PROTECTION_TAKING_DAMAGE_FADEOUT);

                titleReflectionService.sendTitle(this.player, header, footer, fadeIn, stay, fadeOut);
            }
        }
    }
}
