package com.kaosmc.practice.feature.cosmetic.internal.repository;

import com.kaosmc.practice.feature.cosmetic.internal.repository.impl.projectiletrail.ProjectileTrail;
import com.kaosmc.practice.feature.cosmetic.internal.repository.impl.projectiletrail.FlameTrail;

/**
 * @author Remi
 * @project Kaos
 * @date 6/23/2025
 */
public class ProjectileTrailRepository extends BaseCosmeticRepository<ProjectileTrail> {
    public ProjectileTrailRepository() {
        this.registerCosmetic(FlameTrail.class);
    }
}