package com.kaosmc.practice.visual.nametag.internal.strategy;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.adapter.core.Core;
import com.kaosmc.practice.adapter.core.CoreAdapter;
import com.kaosmc.practice.adapter.core.kaoscore.KaosCoreBridge;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NametagFormatResolver {
    private static final Pattern INTEGER_PATTERN = Pattern.compile("-?\\d+");

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

        String tagPrefix = PlayerDisplayUtil.resolveTagPrefix(target);
        String tagColor = ChatColor.WHITE.toString();

        if (core != null) {
            ChatColor coreTagColor = core.getTagColor(target);

            if (coreTagColor != null) {
                tagColor = coreTagColor.toString();
            }
        }

        String nameColor = ChatColor.WHITE.toString();
        if (context.getTargetProfile() != null && context.getTargetProfile().getNameColor() != null) {
            nameColor = context.getTargetProfile().getNameColor().toString();
        }
        String teamColor = resolveTeamColor(context, tagColor);

        String displayName = PlayerDisplayUtil.resolveCurrentNick(target, target.getName());
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

    /**
     * Resolves the target's tab sorting position from PlaceholderAPI.
     * Expected placeholder: %kaoscore_tag_position%
     *
     * @param context Nametag context.
     * @return numeric weight where lower values appear first in tablist.
     */
    public static int resolveSortWeight(NametagContext context) {
        if (context == null || context.getTarget() == null) {
            return 9999;
        }

        KaosCoreBridge kaosCoreBridge = getKaosCoreBridge();
        if (kaosCoreBridge != null) {
            int tagPosition = kaosCoreBridge.getTagPosition(context.getTarget());
            if (tagPosition >= 0 && tagPosition <= 9999) {
                return tagPosition;
            }
        }

        String raw = PlaceholderUtil.setPapiSafe(context.getTarget(), "%kaoscore_tag_position%");
        if (raw == null || raw.trim().isEmpty()) {
            return 9999;
        }

        Matcher matcher = INTEGER_PATTERN.matcher(raw);
        if (!matcher.find()) {
            return 9999;
        }

        try {
            int parsed = Integer.parseInt(matcher.group());
            if (parsed < 0) {
                return 9999;
            }
            return Math.min(parsed, 9999);
        } catch (NumberFormatException ignored) {
            return 9999;
        }
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

    private static KaosCoreBridge getKaosCoreBridge() {
        try {
            return KaosPractice.getInstance().getService(KaosCoreBridge.class);
        } catch (Exception ignored) {
            return null;
        }
    }
}
