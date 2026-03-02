package com.kaosmc.practice.adapter.knockback.internal;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.adapter.knockback.Knockback;
import com.kaosmc.practice.adapter.knockback.KnockbackAdapter;
import com.kaosmc.practice.adapter.knockback.KnockbackType;
import com.kaosmc.practice.bootstrap.AlleyContext;
import com.kaosmc.practice.bootstrap.annotation.Service;
import lombok.Getter;

/**
 * @author Emmy
 * @project Kaos
 * @since 26/04/2025
 */
@Getter
@Service(provides = KnockbackAdapter.class, priority = 50)
public class KnockbackAdapterImpl implements KnockbackAdapter {
    private final KaosPractice plugin;
    private Knockback knockbackImplementation;

    /**
     * Constructor for DI. Receives the main bootstrap instance.
     */
    public KnockbackAdapterImpl(KaosPractice plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.knockbackImplementation = this.determineKnockback();
    }

    @Override
    public Knockback getKnockbackImplementation() {
        if (this.knockbackImplementation == null) {
            throw new IllegalStateException("KnockbackAdapter has not been initialized yet.");
        }
        return this.knockbackImplementation;
    }

    /**
     * Determines the knockback implementation to use based on the server's name.
     *
     * @return The selected knockback implementation.
     */
    private Knockback determineKnockback() {
        Knockback selectedImplementation = new DefaultKnockbackImpl();

        for (KnockbackType kbType : KnockbackType.values()) {
            if (this.plugin.getServer().getName().equalsIgnoreCase(kbType.getSpigotName())) {
                switch (kbType) {
                    case ZONE:
                        selectedImplementation = new ZoneKnockbackImpl();
                        break;
                }
                break;
            }
        }

        return selectedImplementation;
    }
}