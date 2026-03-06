package com.kaosmc.practice.visual.nametag.internal.strategy;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.adapter.core.Core;
import com.kaosmc.practice.adapter.core.CoreAdapter;
import com.kaosmc.practice.common.text.CC;
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

        String safeFormat = format == null || format.trim().isEmpty() ? "{tag_prefix}" : format;
        return CC.translate(safeFormat
                .replace("{tag_prefix}", tagPrefix)
                .replace("{tag-prefix}", tagPrefix)
                .replace("{tag_color}", tagColor)
                .replace("{tag-color}", tagColor)
                .replace("{name_color}", nameColor)
                .replace("{name-color}", nameColor));
    }
}
