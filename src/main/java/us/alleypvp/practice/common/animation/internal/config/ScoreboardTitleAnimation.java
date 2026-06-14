package us.alleypvp.practice.common.animation.internal.config;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.core.config.ConfigService;

/**
 * @author Emmy
 * @project Alley
 * @since 27/03/2025
 */
public class ScoreboardTitleAnimation extends TextAnimation {
    public ScoreboardTitleAnimation() {
        super(AlleyPractice.getInstance().getService(ConfigService.class).getScoreboardConfig(), "scoreboard.title");
    }
}