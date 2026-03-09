package com.kaosmc.practice.feature.cosmetic.internal.repository.impl.soundeffect;

import com.kaosmc.practice.feature.cosmetic.model.CosmeticType;
import com.kaosmc.practice.feature.cosmetic.annotation.CosmeticData;
import com.kaosmc.practice.common.SoundUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Kaos
 * @since 02/04/2025
 */
@CosmeticData(type = CosmeticType.SOUND_EFFECT, name = "Explosion", description = "Toca um som de explosão", permission = "explosion", icon = Material.TNT, slot = 12)
public class ExplosionSoundEffect extends BaseSoundEffect {
    @Override
    public void execute(Player player) {
        SoundUtil.playCustomSound(player, Sound.EXPLODE, 1.0f, 1.0f);
    }
}
