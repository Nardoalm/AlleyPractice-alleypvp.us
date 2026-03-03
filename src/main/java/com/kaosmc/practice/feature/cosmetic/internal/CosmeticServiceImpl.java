package com.kaosmc.practice.feature.cosmetic.internal;

import com.kaosmc.practice.bootstrap.KaosContext;
import com.kaosmc.practice.bootstrap.annotation.Service;
import com.kaosmc.practice.feature.cosmetic.internal.repository.*;
import dev.revere.kaos.feature.cosmetic.internal.repository.*;
import com.kaosmc.practice.feature.cosmetic.CosmeticService;
import com.kaosmc.practice.feature.cosmetic.model.CosmeticType;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author Remi
 * @project Kaos
 * @date 6/1/2024
 */
@Getter
@Service(provides = CosmeticService.class, priority = 140)
public class CosmeticServiceImpl implements CosmeticService {
    private final Map<CosmeticType, BaseCosmeticRepository<?>> repositories = new EnumMap<>(CosmeticType.class);

    @Override
    public void initialize(KaosContext context) {
        this.register(new KillEffectRepository());
        this.register(new SoundEffectRepository());
        this.register(new ProjectileTrailRepository());
        this.register(new KillMessageRepository());
        this.register(new SuitRepository());
        this.register(new CloakRepository());
    }

    /**
     * Registers a repository, using its declared CosmeticType as the key.
     *
     * @param repository The repository instance to register.
     */
    private void register(BaseCosmeticRepository<?> repository) {
        CosmeticType type = repository.getRepositoryType();
        if (type != null) {
            this.repositories.put(type, repository);
        }
    }

    @Override
    public BaseCosmeticRepository<?> getRepository(CosmeticType type) {
        return this.repositories.get(type);
    }

    @Override
    public <T extends BaseCosmeticRepository<?>> T getRepository(CosmeticType type, Class<T> repositoryClass) {
        BaseCosmeticRepository<?> repo = getRepository(type);
        if (repositoryClass.isInstance(repo)) {
            return repositoryClass.cast(repo);
        }
        return null;
    }
}