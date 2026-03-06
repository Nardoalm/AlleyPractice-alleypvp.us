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

        if (match.getKit().isSettingEnabled(KitSettingHideAndSeek.class)) {
            HideAndSeekMatch hideAndSeekMatch = (HideAndSeekMatch) match;
            GameParticipant<?> seekers = hideAndSeekMatch.getParticipantA();
            LocaleService localeService = KaosPractice.getInstance().getService(LocaleService.class);
            String matchFormat = localeService != null
                    ? localeService.getString(SettingsLocaleImpl.VISUALS_NAMETAG_MATCH_FORMAT)
                    : "{tag_color}";
            String matchPrefix = NametagFormatResolver.resolve(matchFormat, context);

            boolean viewerIsSeeker = seekers.containsPlayer(context.getViewer().getUniqueId());
            boolean targetIsSeeker = seekers.containsPlayer(context.getTarget().getUniqueId());

            if (viewerIsSeeker) {
                if (targetIsSeeker) {
                    return new NametagView(matchPrefix, "", NametagVisibility.ALWAYS);
                } else {
                    return new NametagView(matchPrefix, "", NametagVisibility.NEVER);
                }
            } else {
                if (targetIsSeeker) {
                    return new NametagView(matchPrefix, "", NametagVisibility.ALWAYS);
                } else {
                    return new NametagView(matchPrefix, "", NametagVisibility.ALWAYS);
                }
            }
        }

        LocaleService localeService = KaosPractice.getInstance().getService(LocaleService.class);
        String matchFormat = localeService != null
                ? localeService.getString(SettingsLocaleImpl.VISUALS_NAMETAG_MATCH_FORMAT)
                : "{tag_color}";
        String prefix = NametagFormatResolver.resolve(matchFormat, context);
        return new NametagView(prefix, "");
    }
}
