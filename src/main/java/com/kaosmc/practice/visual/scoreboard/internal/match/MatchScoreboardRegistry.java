package com.kaosmc.practice.visual.scoreboard.internal.match;

import com.kaosmc.practice.feature.kit.setting.KitSetting;
import com.kaosmc.practice.feature.match.Match;
import com.kaosmc.practice.visual.scoreboard.internal.match.annotation.ScoreboardData;
import com.kaosmc.practice.common.logger.Logger;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Kaos
 * @since 26/06/2025
 */
public class MatchScoreboardRegistry {
    private final Map<Class<? extends KitSetting>, MatchScoreboard> kitSettingScoreboards = new HashMap<>();
    private final Map<Class<? extends Match>, MatchScoreboard> matchTypeScoreboards = new HashMap<>();
    private MatchScoreboard defaultScoreboard;

    /**
     * Scans the classpath to discover and register all annotated scoreboard providers.
     * This should be called once by the object that creates the registry.
     */
    public void initialize() {
        String searchPackage = "com.kaosmc.practice.visual.scoreboard";

        try (ScanResult scanResult = new ClassGraph().enableAllInfo().acceptPackages(searchPackage).scan()) {
            for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(ScoreboardData.class.getName())) {
                if (classInfo.isAbstract() || classInfo.isInterface()) {
                    continue;
                }

                try {
                    Class<?> clazz = classInfo.loadClass();
                    if (!MatchScoreboard.class.isAssignableFrom(clazz)) {
                        continue;
                    }

                    Constructor<?> constructor = clazz.getConstructor();

                    MatchScoreboard scoreboard = (MatchScoreboard) constructor.newInstance();
                    ScoreboardData annotation = clazz.getAnnotation(ScoreboardData.class);

                    assert annotation != null;
                    if (annotation.isDefault()) {
                        this.defaultScoreboard = scoreboard;
                    } else if (annotation.kit() != KitSetting.class) {
                        kitSettingScoreboards.put(annotation.kit(), scoreboard);
                    } else if (annotation.match() != Match.class) {
                        matchTypeScoreboards.put(annotation.match(), scoreboard);
                    }
                } catch (Exception e) {
                    Logger.logException("Falha ao instanciar o visual da scoreboard: " + classInfo.getName(), e);
                }
            }
        }
    }

    /**
     * Resolves the appropriate scoreboard for the given match.
     *
     * @param match The match to resolve.
     * @return The resolved IMatchScoreboard.
     */
    public MatchScoreboard getScoreboard(Match match) {
        for (Class<? extends KitSetting> settingClass : kitSettingScoreboards.keySet()) {
            if (match.getKit().isSettingEnabled(settingClass)) {
                return kitSettingScoreboards.get(settingClass);
            }
        }

        MatchScoreboard scoreboard = matchTypeScoreboards.get(match.getClass());
        if (scoreboard != null) {
            return scoreboard;
        }

        return defaultScoreboard;
    }
}
