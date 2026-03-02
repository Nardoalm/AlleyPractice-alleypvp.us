package com.kaosmc.practice.adapter.knockback;

import com.kaosmc.practice.bootstrap.lifecycle.Service;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface KnockbackAdapter extends Service {
    /**
     * Gets the active knockback implementation that was detected during startup.
     *
     * @return The IKnockback implementation for the current server type.
     */
    Knockback getKnockbackImplementation();
}