package com.kaosmc.practice.visual.nametag.internal.strategy;

import com.kaosmc.practice.visual.nametag.model.NametagContext;
import com.kaosmc.practice.visual.nametag.NametagView;

/**
 * @author Remi
 * @project kaos-practice
 * @date 27/06/2025
 */
public interface NametagStrategy {
    /**
     * Creates a NametagView based on the given context.
     *
     * @param context The context containing the viewer and target.
     * @return A NametagView if this strategy applies, otherwise null.
     */
    NametagView createNametagView(NametagContext context);
}