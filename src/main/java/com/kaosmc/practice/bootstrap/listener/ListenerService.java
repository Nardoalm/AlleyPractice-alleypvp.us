package com.kaosmc.practice.bootstrap.listener;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.bootstrap.lifecycle.Service;

/**
 * @author Emmy
 * @project kaos-practice
 * @since 16/07/2025
 */
public interface ListenerService extends Service {

    /**
     * Registers all listeners for the service.
     * This method should be called during the service initialization phase
     */
    void registerListeners(KaosPractice plugin);
}