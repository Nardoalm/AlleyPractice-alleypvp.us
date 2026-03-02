package com.kaosmc.practice.feature.cosmetic.internal.repository.impl.killeffect;

import com.kaosmc.practice.feature.cosmetic.model.CosmeticType;
import com.kaosmc.practice.feature.cosmetic.annotation.CosmeticData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Kaos
 * @date 01/06/2024
 */
@CosmeticData(type = CosmeticType.KILL_EFFECT, name = "Thunder", description = "Spawn a lighting bolt at the opponent", permission = "thunder", icon = Material.STICK, slot = 11)
public class ThunderKillEffect extends BaseKillEffect {
    @Override
    public void execute(Player player) {
        Location location = player.getLocation();
        location.getWorld().strikeLightningEffect(location);
    }
}
