package us.alleypvp.practice.adapter.knockback;

import us.alleypvp.practice.bootstrap.lifecycle.Service;

/**
 * @author Remi
 * @project kaos-practice
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