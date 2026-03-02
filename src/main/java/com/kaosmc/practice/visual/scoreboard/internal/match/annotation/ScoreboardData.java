package com.kaosmc.practice.visual.scoreboard.internal.match.annotation;

import com.kaosmc.practice.feature.kit.setting.KitSetting;
import com.kaosmc.practice.feature.match.Match;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Remi
 * @project Kaos
 * @since 26/06/2025
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScoreboardData {
    Class<? extends Match> match() default Match.class;
    Class<? extends KitSetting> kit() default KitSetting.class;
    boolean isDefault() default false;
}