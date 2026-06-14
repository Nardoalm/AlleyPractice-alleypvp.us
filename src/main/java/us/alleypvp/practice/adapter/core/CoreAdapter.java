package us.alleypvp.practice.adapter.core;

import us.alleypvp.practice.bootstrap.lifecycle.Service;

/**
 * @author Remi
 * @project kaos-practice
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