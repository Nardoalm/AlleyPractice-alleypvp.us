package us.alleypvp.practice.feature.cosmetic.internal.repository;

import lombok.Getter;
import us.alleypvp.practice.feature.cosmetic.internal.repository.impl.killeffect.*;

/**
 * @author Remi
 * @project Alley
 * @date 01/06/2024
 */
@Getter
public class KillEffectRepository extends BaseCosmeticRepository<BaseKillEffect> {
    public KillEffectRepository() {
        this.registerCosmetic(BloodKillEffect.class);
        this.registerCosmetic(ExplosionKillEffect.class);
        this.registerCosmetic(FireworkKillEffect.class);
        this.registerCosmetic(HeartKillEffect.class);
        this.registerCosmetic(NoneKillEffect.class);
        this.registerCosmetic(ThunderKillEffect.class);
    }
}
