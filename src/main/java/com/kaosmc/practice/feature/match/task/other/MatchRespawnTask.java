package com.kaosmc.practice.feature.match.task.other;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.reflect.ReflectionService;
import com.kaosmc.practice.common.reflect.internal.types.TitleReflectionServiceImpl;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.VisualsLocaleImpl;
import com.kaosmc.practice.core.locale.internal.impl.message.GameMessagesLocaleImpl;
import com.kaosmc.practice.feature.match.Match;
import com.kaosmc.practice.feature.match.MatchState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

/**
 * @author Emmy
 * @project Kaos
 * @since 23/05/2025
 */
public class MatchRespawnTask extends BukkitRunnable {
    protected final Player player;
    protected final Match match;
    private int count;

    /**
     * Constructor for the MatchRespawnTask class.
     *
     * @param player The player to respawn.
     * @param match  The match instance.
     * @param count  The countdown time in seconds.
     */
    public MatchRespawnTask(Player player, Match match, int count) {
        this.player = player;
        this.match = match;
        this.count = count;
    }

    @Override
    public void run() {
        if (this.count == 0) {
            this.cancel();
            this.match.handleRespawn(this.player);
            return;
        }

        if (this.match.getState() == MatchState.ENDING_MATCH || this.match.getState() == MatchState.ENDING_ROUND) {
            this.cancel();
            return;
        }

        LocaleService localeService = KaosPractice.getInstance().getService(LocaleService.class);
        if (localeService.getBoolean(VisualsLocaleImpl.TITLE_MATCH_RESPAWNING_ENABLED_BOOLEAN)) {
            String header = localeService.getString(VisualsLocaleImpl.TITLE_MATCH_RESPAWNING_HEADER).replace("{seconds}", String.valueOf(this.count));
            String footer = localeService.getString(VisualsLocaleImpl.TITLE_MATCH_RESPAWNING_FOOTER).replace("{seconds}", String.valueOf(this.count));
            int fadeIn = localeService.getInt(VisualsLocaleImpl.TITLE_MATCH_RESPAWNING_FADE_IN);
            int stay = localeService.getInt(VisualsLocaleImpl.TITLE_MATCH_RESPAWNING_STAY);
            int fadeOut = localeService.getInt(VisualsLocaleImpl.TITLE_MATCH_RESPAWNING_FADEOUT);

            KaosPractice.getInstance().getService(ReflectionService.class).getReflectionService(TitleReflectionServiceImpl.class).sendTitle(
                    player,
                    header,
                    footer,
                    fadeIn, stay, fadeOut
            );
        }

        List<String> messageFormat = localeService.getStringList(GameMessagesLocaleImpl.MATCH_RESPAWNING_MESSAGE_FORMAT);
        if (localeService.getBoolean(GameMessagesLocaleImpl.MATCH_RESPAWNING_MESSAGE_ENABLED_BOOLEAN)) {
            messageFormat.forEach(message -> this.player.sendMessage(CC.translate(message.replace("{seconds}", String.valueOf(this.count)))));
        }

        this.count--;
    }
}