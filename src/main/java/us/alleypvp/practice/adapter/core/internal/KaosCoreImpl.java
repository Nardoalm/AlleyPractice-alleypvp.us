package us.alleypvp.practice.adapter.core.internal;

import us.alleypvp.practice.adapter.core.Core;
import us.alleypvp.practice.adapter.core.CoreType;
import us.alleypvp.practice.adapter.core.kaoscore.KaosCoreBridge;
import us.alleypvp.practice.common.text.CC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KaosCoreImpl implements Core {
    private final KaosCoreBridge kaosCoreBridge;

    public KaosCoreImpl(KaosCoreBridge kaosCoreBridge) {
        this.kaosCoreBridge = kaosCoreBridge;
    }

    @Override
    public CoreType getType() {
        return CoreType.KAOSCORE;
    }

    @Override
    public ChatColor getPlayerColor(Player player) {
        return this.getTagColor(player);
    }

    @Override
    public String getRankPrefix(Player player) {
        return CC.translate(this.kaosCoreBridge.getTagPrefix(player));
    }

    @Override
    public String getRankName(Player player) {
        return this.kaosCoreBridge.getTagName(player);
    }

    @Override
    public String getRankSuffix(Player player) {
        return CC.translate(this.kaosCoreBridge.getTagSuffix(player));
    }

    @Override
    public ChatColor getRankColor(Player player) {
        return this.getTagColor(player);
    }

    @Override
    public String getTagPrefix(Player player) {
        return CC.translate(this.kaosCoreBridge.getTagPrefix(player));
    }

    @Override
    public ChatColor getTagColor(Player player) {
        ChatColor tagColor = this.kaosCoreBridge.getTagColor(player);
        return tagColor != null ? tagColor : ChatColor.WHITE;
    }
}
