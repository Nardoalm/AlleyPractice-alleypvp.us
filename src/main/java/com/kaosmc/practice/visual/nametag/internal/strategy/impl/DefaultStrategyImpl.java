package com.kaosmc.practice.visual.nametag.internal.strategy.impl;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.SettingsLocaleImpl;
import com.kaosmc.practice.visual.nametag.NametagVisibility;
import com.kaosmc.practice.visual.nametag.model.NametagContext;
import com.kaosmc.practice.visual.nametag.NametagView;
import com.kaosmc.practice.visual.nametag.internal.strategy.NametagFormatResolver;
import com.kaosmc.practice.visual.nametag.internal.strategy.NametagStrategy;

/**
 * @author Remi
 * @project kaos-practice
 * @date 27/06/2025
 */
public class DefaultStrategyImpl implements NametagStrategy {
    @Override
    public NametagView createNametagView(NametagContext context) {
        if (context == null || context.getTarget() == null) {
            return null;
        }

        LocaleService localeService = KaosPractice.getInstance().getService(LocaleService.class);
        String lobbyFormat = localeService != null
                ? localeService.getString(SettingsLocaleImpl.VISUALS_NAMETAG_LOBBY_FORMAT)
                : "{tag_prefix}";
        String lobbySuffix = localeService != null
                ? localeService.getString(SettingsLocaleImpl.VISUALS_NAMETAG_LOBBY_SUFFIX)
                : "";
        String prefix = NametagFormatResolver.resolve(lobbyFormat, context);
        String suffix = NametagFormatResolver.resolve(lobbySuffix, context);

        if (prefix == null || prefix.trim().isEmpty()) {
            prefix = NametagFormatResolver.resolve("{tag_prefix}", context);
        }

        return new NametagView(prefix, suffix, NametagVisibility.ALWAYS, NametagFormatResolver.resolveSortWeight(context));
    }
}
