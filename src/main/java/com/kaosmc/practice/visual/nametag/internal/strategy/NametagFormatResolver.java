package com.kaosmc.practice.visual.nametag.internal.strategy;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.adapter.core.Core;
import com.kaosmc.practice.adapter.core.CoreAdapter;
import com.kaosmc.practice.common.PlayerDisplayUtil;
import com.kaosmc.practice.common.PlaceholderUtil;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.feature.match.Match;
import com.kaosmc.practice.feature.match.internal.types.DefaultMatch;
import com.kaosmc.practice.feature.match.model.GameParticipant;
import com.kaosmc.practice.feature.match.model.internal.MatchGamePlayer;
import com.kaosmc.practice.visual.nametag.model.NametagContext;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class NametagFormatResolver {
    private NametagFormatResolver() {
    }

    public static String resolve(String format, NametagContext context) {
        if (context == null) {
            return ChatColor.GRAY.toString();
        }

        Player target = context.getTarget();
        if (target == null) {
            return ChatColor.GRAY.toString();
        }

        Core core = null;
        CoreAdapter coreAdapter = KaosPractice.getInstance().getService(CoreAdapter.class);
        if (coreAdapter != null) {
            core = coreAdapter.getCore();
        }

        String tagPrefix = "";
        String tagColor = ChatColor.WHITE.toString();

        if (core != null) {
            String coreTagPrefix = core.getTagPrefix(target);
            ChatColor coreTagColor = core.getTagColor(target);

            if (coreTagPrefix != null) {
                tagPrefix = CC.translate(coreTagPrefix);
            }
            if (coreTagColor != null) {
                tagColor = coreTagColor.toString();
            }
        }

        String nameColor = ChatColor.WHITE.toString();
        if (context.getTargetProfile() != null && context.getTargetProfile().getNameColor() != null) {
            nameColor = context.getTargetProfile().getNameColor().toString();
        }
        String teamColor = resolveTeamColor(context, tagColor);

        String displayName = PlayerDisplayUtil.resolveDisplayName(target, target.getName());
        String safeFormat = format == null || format.trim().isEmpty() ? "{tag_prefix}" : format;
        String resolved = safeFormat
                .replace("{tag_prefix}", tagPrefix)
                .replace("{tag-prefix}", tagPrefix)
                .replace("{tag_color}", tagColor)
                .replace("{tag-color}", tagColor)
                .replace("{name_color}", nameColor)
                .replace("{name-color}", nameColor)
                .replace("{team_color}", teamColor)
                .replace("{team-color}", teamColor)
                .replace("{nick}", displayName)
                .replace("{name}", displayName)
                .replace("{player}", displayName);

        resolved = PlaceholderUtil.setPapiSafe(target, resolved);
        return CC.translate(resolved);
    }

    private static String resolveTeamColor(NametagContext context, String fallbackColor) {
        if (context == null || context.getViewerProfile() == null || context.getTargetProfile() == null) {
            return fallbackColor;
        }

        Match viewerMatch = context.getViewerProfile().getMatch();
        Match targetMatch = context.getTargetProfile().getMatch();
        if (!(viewerMatch instanceof DefaultMatch) || viewerMatch != targetMatch) {
            return fallbackColor;
        }

        DefaultMatch defaultMatch = (DefaultMatch) viewerMatch;
        GameParticipant<MatchGamePlayer> targetParticipant = defaultMatch.getParticipant(context.getTarget());
        if (targetParticipant == null) {
            return fallbackColor;
        }

        ChatColor color = defaultMatch.getTeamColor(targetParticipant);
        return color != null ? color.toString() : fallbackColor;
    }
}
