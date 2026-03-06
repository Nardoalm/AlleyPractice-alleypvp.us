package com.kaosmc.practice.visual.nametag.internal.strategy.impl;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.SettingsLocaleImpl;
import com.kaosmc.practice.visual.nametag.NametagVisibility;
import com.kaosmc.practice.visual.nametag.model.NametagContext;
import com.kaosmc.practice.visual.nametag.NametagView;
import com.kaosmc.practice.visual.nametag.internal.strategy.NametagFormatResolver;
import com.kaosmc.practice.visual.nametag.internal.strategy.NametagStrategy;
import com.kaosmc.practice.feature.match.Match;
import com.kaosmc.practice.feature.match.model.GameParticipant;
import com.kaosmc.practice.core.profile.enums.ProfileState;

/**
 * @author Remi
 * @project kaos-practice
 * @date 27/06/2025
 */
public class SpectatorStrategyImpl implements NametagStrategy {
    @Override
    public NametagView createNametagView(NametagContext context) {
        if (context == null
                || context.getViewerProfile() == null
                || context.getTargetProfile() == null
                || context.getTarget() == null) {
            return null;
        }

        if (context.getViewerProfile().getState() != ProfileState.SPECTATING) {
            return null;
        }

        if (context.getTargetProfile().getState() != ProfileState.PLAYING) {
            return null;
        }

        Match match = context.getViewerProfile().getMatch();
        if (match == null || !match.equals(context.getTargetProfile().getMatch())) {
            return null;
        }

        GameParticipant<?> targetParticipant = match.getParticipant(context.getTarget());
        if (targetParticipant == null) {
            return null;
        }

        LocaleService localeService = KaosPractice.getInstance().getService(LocaleService.class);
        String matchFormat = localeService != null
                ? localeService.getString(SettingsLocaleImpl.VISUALS_NAMETAG_MATCH_FORMAT)
                : "{tag_color}";
        String prefix = NametagFormatResolver.resolve(matchFormat, context);
        return new NametagView(prefix, "", NametagVisibility.ALWAYS, NametagFormatResolver.resolveSortWeight(context));
    }
}
