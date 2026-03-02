package com.kaosmc.practice.feature.cosmetic.internal.repository;

import com.kaosmc.practice.feature.cosmetic.internal.repository.impl.soundeffect.BaseSoundEffect;
import com.kaosmc.practice.feature.cosmetic.internal.repository.impl.soundeffect.ExplosionSoundEffect;
import com.kaosmc.practice.feature.cosmetic.internal.repository.impl.soundeffect.NoneSoundEffect;
import com.kaosmc.practice.feature.cosmetic.internal.repository.impl.soundeffect.StepSoundEffect;
import lombok.Getter;

/**
 * @author Remi
 * @project Kaos
 * @date 01/06/2024
 */
@Getter
public class SoundEffectRepository extends BaseCosmeticRepository<BaseSoundEffect> {
    public SoundEffectRepository() {
        this.registerCosmetic(ExplosionSoundEffect.class);
        this.registerCosmetic(NoneSoundEffect.class);
        this.registerCosmetic(StepSoundEffect.class);
    }
}