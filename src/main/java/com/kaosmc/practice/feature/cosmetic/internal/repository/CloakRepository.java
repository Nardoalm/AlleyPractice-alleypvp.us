package com.kaosmc.practice.feature.cosmetic.internal.repository;

import com.kaosmc.practice.feature.cosmetic.internal.repository.impl.cloak.*;
import dev.revere.kaos.feature.cosmetic.internal.repository.impl.cloak.*;

/**
 * @author Remi
 * @project kaos-practice
 * @date 4/08/2025
 */
public class CloakRepository extends BaseCosmeticRepository<BaseCloak> {
    public CloakRepository() {
        registerCosmetic(NoneCloak.class);
        registerCosmetic(AngelWingsCloak.class);
        registerCosmetic(HaloCloak.class);
        registerCosmetic(VailCloak.class);
    }
}
