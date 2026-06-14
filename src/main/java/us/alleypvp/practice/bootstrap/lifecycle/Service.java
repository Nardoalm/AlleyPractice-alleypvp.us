package us.alleypvp.practice.bootstrap.lifecycle;

import us.alleypvp.practice.bootstrap.KaosContext;

/**
 * @author Remi
 * @project kaos-practice
 * @date 2/07/2025
 */
public interface Service {
    /**
     * Called by the KaosContext *after* the service instance has been created
     * and its server dependencies are available, but not necessarily fully initialized.
     *
     * @param context The application context, for access to the bootstrap instance or other services.
     */
    default void setup(KaosContext context) {
        // Default implementation: no-op
    }

    /**
     * Called by the KaosContext *after* all services have been created and setup.
     * Use this for logic that requires other services to be fully operational,
     * such as registering listeners or loading data from other services.
     *
     * @param context The application context.
     */
    default void initialize(KaosContext context) {
        // Default implementation: no-op
    }

    /**
     * Called by the KaosContext during bootstrap shutdown.
     * Should be used to release resources, save data, etc.
     *
     * @param context The application context.
     */
    default void shutdown(KaosContext context) {
        // Default implementation: no-op
    }
}