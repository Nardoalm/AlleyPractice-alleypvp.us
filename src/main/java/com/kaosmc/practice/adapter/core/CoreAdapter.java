package com.kaosmc.practice.adapter.core;

import com.kaosmc.practice.bootstrap.lifecycle.Service;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface CoreAdapter extends Service {

    /**
     * Gets the active core implementation that was detected during startup.
     *
     * @return The Core implementation for the currently enabled core bootstrap.
     */
    Core getCore();
}