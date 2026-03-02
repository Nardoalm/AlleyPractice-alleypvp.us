package com.kaosmc.practice.visual.nametag.internal.strategy.impl;

import com.kaosmc.practice.visual.nametag.model.NametagContext;
import com.kaosmc.practice.visual.nametag.NametagView;
import com.kaosmc.practice.visual.nametag.internal.strategy.NametagStrategy;
import com.kaosmc.practice.common.text.CC;
import org.bukkit.ChatColor;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
public class DefaultStrategyImpl implements NametagStrategy {
    @Override
    public NametagView createNametagView(NametagContext context) {
        String prefix = context.getTargetProfile().getNameColor().toString();

        if (prefix == null || prefix.isEmpty()) {
            prefix = ChatColor.GRAY.toString();
        }

        return new NametagView(CC.translate(prefix), "");
    }
}