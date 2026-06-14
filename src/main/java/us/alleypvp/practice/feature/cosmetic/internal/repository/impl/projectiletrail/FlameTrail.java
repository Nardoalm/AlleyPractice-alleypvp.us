package us.alleypvp.practice.feature.cosmetic.internal.repository.impl.projectiletrail;

import us.alleypvp.practice.feature.cosmetic.model.CosmeticType;
import us.alleypvp.practice.feature.cosmetic.annotation.CosmeticData;
import us.alleypvp.practice.common.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

/**
 * @author Remi
 * @project Alley
 * @date 6/23/2025
 */
@CosmeticData(
        type = CosmeticType.PROJECTILE_TRAIL,
        name = "Flame",
        description = "Sair a blazing trail behind your projectiles!",
        icon = Material.BLAZE_POWDER,
        slot = 10,
        price = 1000
)
public class FlameTrail extends ProjectileTrail {
    @Override
    public void spawnTrailParticle(Location location) {
        World world = location.getWorld();
        if (world == null) return;
        ParticleEffect.FLAME.display(0F, 0F, 0F, 0F, 1, location, 64.0);
        ParticleEffect.SMOKE_LARGE.display(0F, 0F, 0F, 0F, 1, location, 64.0);
    }
}