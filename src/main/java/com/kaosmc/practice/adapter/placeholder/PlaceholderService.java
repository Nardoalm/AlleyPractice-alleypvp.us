package com.kaosmc.practice.adapter.placeholder;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.bootstrap.lifecycle.Service;

/**
 * @author Emmy
 * @project kaos-practice
 * @since 17/07/2025
 */
public interface PlaceholderService extends Service {
    /**
     * Registers a papi expansion bootstrap with the Kaos bootstrap.
     *
     * @param plugin The Kaos bootstrap instance to register.
     */
    void registerExpansion(KaosPractice plugin);
}
