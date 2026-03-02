package com.kaosmc.practice.visual.nametag.internal.strategy.impl;

import com.kaosmc.practice.feature.kit.setting.types.mode.KitSettingHideAndSeek;
import com.kaosmc.practice.visual.nametag.model.NametagContext;
import com.kaosmc.practice.visual.nametag.NametagView;
import com.kaosmc.practice.visual.nametag.NametagVisibility;
import com.kaosmc.practice.visual.nametag.internal.strategy.NametagStrategy;
import com.kaosmc.practice.feature.match.Match;
import com.kaosmc.practice.feature.match.internal.types.HideAndSeekMatch;
import com.kaosmc.practice.feature.match.model.GameParticipant;
import com.kaosmc.practice.core.profile.enums.ProfileState;
import com.kaosmc.practice.common.text.CC;
import org.bukkit.ChatColor;

/**
 * @author Remi
 * @project alley-practice
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

            boolean viewerIsSeeker = seekers.containsPlayer(context.getViewer().getUniqueId());
            boolean targetIsSeeker = seekers.containsPlayer(context.getTarget().getUniqueId());

            if (viewerIsSeeker) {
                if (targetIsSeeker) {
                    return new NametagView(CC.translate("&a"), "", NametagVisibility.ALWAYS);
                } else {
                    return new NametagView(CC.translate("&c"), "", NametagVisibility.NEVER);
                }
            } else {
                if (targetIsSeeker) {
                    return new NametagView(CC.translate("&c"), "", NametagVisibility.ALWAYS);
                } else {
                    return new NametagView(CC.translate("&a"), "", NametagVisibility.ALWAYS);
                }
            }
        }

        if (!match.isTeamMatch()) {
            return null;
        }

        if (match.isInSameTeam(context.getViewer(), context.getTarget())) {
            return new NametagView(CC.translate(ChatColor.GREEN.toString()), "");
        } else {
            return new NametagView(CC.translate(ChatColor.RED.toString()), "");
        }
    }
}