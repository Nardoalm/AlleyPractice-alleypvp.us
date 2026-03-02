package com.kaosmc.practice.common.animation.internal.config;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.core.config.ConfigService;

/**
 * @author Emmy
 * @project Kaos
 * @since 27/03/2025
 */
public class ScoreboardTitleAnimation extends TextAnimation {
    public ScoreboardTitleAnimation() {
        super(KaosPractice.getInstance().getService(ConfigService.class).getScoreboardConfig(), "scoreboard.title");
    }
}