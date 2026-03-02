package com.kaosmc.practice.visual.nametag.internal.strategy.impl;

import com.kaosmc.practice.visual.nametag.model.NametagContext;
import com.kaosmc.practice.visual.nametag.NametagView;
import com.kaosmc.practice.visual.nametag.internal.strategy.NametagStrategy;
import com.kaosmc.practice.feature.match.Match;
import com.kaosmc.practice.feature.match.model.GameParticipant;
import com.kaosmc.practice.core.profile.enums.ProfileState;
import com.kaosmc.practice.common.text.CC;
import org.bukkit.ChatColor;

import java.util.List;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
public class SpectatorStrategyImpl implements NametagStrategy {
    @Override
    public NametagView createNametagView(NametagContext context) {
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

        List<? extends GameParticipant<?>> participantsInMatch = match.getParticipants();

        int teamIndex = participantsInMatch.indexOf(targetParticipant);

        if (teamIndex == 0) {
            return new NametagView(CC.translate(ChatColor.BLUE.toString()), "");
        } else if (teamIndex == 1) {
            return new NametagView(CC.translate(ChatColor.RED.toString()), "");
        }

        return null;
    }
}