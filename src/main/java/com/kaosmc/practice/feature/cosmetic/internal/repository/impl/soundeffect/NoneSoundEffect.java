package com.kaosmc.practice.feature.cosmetic.internal.repository.impl.soundeffect;

import com.kaosmc.practice.feature.cosmetic.model.CosmeticType;
import com.kaosmc.practice.feature.cosmetic.annotation.CosmeticData;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Kaos
 * @date 01/06/2024
 */
@CosmeticData(type = CosmeticType.SOUND_EFFECT, name = "None", description = "Remover your sound effect", icon = Material.BARRIER, slot = 10)
public class NoneSoundEffect extends BaseSoundEffect {
    @Override
    public void execute(Player player) {

    }
}
