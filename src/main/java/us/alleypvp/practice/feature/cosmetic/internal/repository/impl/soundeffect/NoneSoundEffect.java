package us.alleypvp.practice.feature.cosmetic.internal.repository.impl.soundeffect;

import us.alleypvp.practice.feature.cosmetic.model.CosmeticType;
import us.alleypvp.practice.feature.cosmetic.annotation.CosmeticData;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 01/06/2024
 */
@CosmeticData(type = CosmeticType.SOUND_EFFECT, name = "None", description = "Remova seu efeito sonoro", icon = Material.BARRIER, slot = 10)
public class NoneSoundEffect extends BaseSoundEffect {
    @Override
    public void execute(Player player) {

    }
}
