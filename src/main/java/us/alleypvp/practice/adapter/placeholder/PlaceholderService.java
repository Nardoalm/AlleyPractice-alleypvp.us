package us.alleypvp.practice.adapter.placeholder;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.bootstrap.lifecycle.Service;

/**
 * @author Emmy
 * @project kaos-practice
 * @since 17/07/2025
 */
public interface PlaceholderService extends Service {
    /**
     * Registers a papi expansion bootstrap with the Alley bootstrap.
     *
     * @param plugin The Alley bootstrap instance to register.
     */
    void registerExpansion(AlleyPractice plugin);
}
