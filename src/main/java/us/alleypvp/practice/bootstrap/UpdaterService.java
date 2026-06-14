package us.alleypvp.practice.bootstrap;

import us.alleypvp.practice.bootstrap.lifecycle.Service;

import java.io.IOException;

/**
 * @author Remi
 * @project kaos-practice
 * @date 24/07/2025
 */
public interface UpdaterService extends Service {
    /**
     * Checks for updates to the bootstrap by comparing the current version with the latest version available on GitHub.
     * If an update is available, it logs a warning message.
     */
    void checkForUpdates();

    /**
     * Downloads the latest version of the bootstrap and updates it.
     *
     * @param version The version to download and update to.
     */
    void downloadAndUpdate(String version);

    /**
     * Fetches the latest version of the bootstrap from GitHub.
     *
     * @return The latest version as a String.
     * @throws IOException If there is an error fetching the version.
     */
    String getLatestVersion() throws IOException;
}
