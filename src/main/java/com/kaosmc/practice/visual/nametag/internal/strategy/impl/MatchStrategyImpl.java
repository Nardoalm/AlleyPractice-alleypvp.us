package com.kaosmc.practice.visual.nametag.internal.strategy.impl;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.SettingsLocaleImpl;
import com.kaosmc.practice.feature.kit.setting.types.mode.KitSettingHideAndSeek;
import com.kaosmc.practice.visual.nametag.model.NametagContext;
import com.kaosmc.practice.visual.nametag.NametagView;
import com.kaosmc.practice.visual.nametag.NametagVisibility;
import com.kaosmc.practice.visual.nametag.internal.strategy.NametagFormatResolver;
import com.kaosmc.practice.visual.nametag.internal.strategy.NametagStrategy;
import com.kaosmc.practice.feature.match.Match;
import com.kaosmc.practice.feature.match.internal.types.HideAndSeekMatch;
import com.kaosmc.practice.feature.match.model.GameParticipant;
import com.kaosmc.practice.core.profile.enums.ProfileState;

/**
 * @author Remi
 * @project kaos-practice
 * @date 27/06/2025
 */
public class MatchStrategyImpl implements NametagStrategy {
    @Override
    public NametagView createNametagView(NametagContext context) {
        if (context == null
                || context.getViewerProfile() == null
                || context.getTargetProfile() == null
                || context.getViewer() == null
                || context.getTarget() == null) {
            return null;
        }

        if (context.getViewerProfile().getState() != ProfileState.PLAYING) {
            return null;
        }

        Match match = context.getViewerProfile().getMatch();
        if (match == null) {
            return null;
        }

        if (context.getTargetProfile().getMatch() == null || !context.getTargetProfile().getMatch().equals(match)) {
            return null;
        }

        if (match.getKit() != null && match.getKit().isSettingEnabled(KitSettingHideAndSeek.class)) {
            HideAndSeekMatch hideAndSeekMatch = (HideAndSeekMatch) match;
            GameParticipant<?> seekers = hideAndSeekMatch.getParticipantA();
            LocaleService localeService = KaosPractice.getInstance().getService(LocaleService.class);
            String matchFormat = localeService != null
                    ? localeService.getString(SettingsLocaleImpl.VISUALS_NAMETAG_MATCH_FORMAT)
                    : "{team_color}";
            String matchSuffix = localeService != null
                    ? localeService.getString(SettingsLocaleImpl.VISUALS_NAMETAG_MATCH_SUFFIX)
                    : "";
            String matchPrefix = NametagFormatResolver.resolve(matchFormat, context);
            String suffix = NametagFormatResolver.resolve(matchSuffix, context);

            boolean viewerIsSeeker = seekers.containsPlayer(context.getViewer().getUniqueId());
            boolean targetIsSeeker = seekers.containsPlayer(context.getTarget().getUniqueId());
            int sortWeight = NametagFormatResolver.resolveSortWeight(context);
            String groupKey = NametagFormatResolver.resolveMatchGroupKey(context);

            if (viewerIsSeeker) {
                if (targetIsSeeker) {
                    return new NametagView(matchPrefix, suffix, NametagVisibility.ALWAYS, sortWeight, groupKey);
                } else {
                    return new NametagView(matchPrefix, suffix, NametagVisibility.NEVER, sortWeight, groupKey);
                }
            } else {
                if (targetIsSeeker) {
                    return new NametagView(matchPrefix, suffix, NametagVisibility.ALWAYS, sortWeight, groupKey);
                } else {
                    return new NametagView(matchPrefix, suffix, NametagVisibility.ALWAYS, sortWeight, groupKey);
                }
            }
        }

        LocaleService localeService = KaosPractice.getInstance().getService(LocaleService.class);
        String matchFormat = localeService != null
                ? localeService.getString(SettingsLocaleImpl.VISUALS_NAMETAG_MATCH_FORMAT)
                : "{team_color}";
        String matchSuffix = localeService != null
                ? localeService.getString(SettingsLocaleImpl.VISUALS_NAMETAG_MATCH_SUFFIX)
                : "";
        String prefix = NametagFormatResolver.resolve(matchFormat, context);
        String suffix = NametagFormatResolver.resolve(matchSuffix, context);

        if (prefix == null || prefix.trim().isEmpty()) {
            prefix = NametagFormatResolver.resolve("{team_color}", context);
        }

        return new NametagView(
                prefix,
                suffix,
                NametagVisibility.ALWAYS,
                NametagFormatResolver.resolveSortWeight(context),
                NametagFormatResolver.resolveMatchGroupKey(context)
        );
    }
}
