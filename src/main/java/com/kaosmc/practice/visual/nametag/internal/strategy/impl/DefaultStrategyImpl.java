package com.kaosmc.practice.visual.nametag.internal.strategy.impl;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.SettingsLocaleImpl;
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
        LocaleService localeService = KaosPractice.getInstance().getService(LocaleService.class);
        String lobbyFormat = localeService != null
                ? localeService.getString(SettingsLocaleImpl.VISUALS_NAMETAG_LOBBY_FORMAT)
                : "{tag_prefix}";
        String prefix = NametagFormatResolver.resolve(lobbyFormat, context);

        if (prefix == null || prefix.trim().isEmpty()) {
            prefix = NametagFormatResolver.resolve("{tag_color}", context);
        }

        return new NametagView(prefix, "");
    }
}
