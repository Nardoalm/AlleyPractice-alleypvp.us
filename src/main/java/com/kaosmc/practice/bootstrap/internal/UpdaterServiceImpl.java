package com.kaosmc.practice.bootstrap.internal;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.bootstrap.KaosContext;
import com.kaosmc.practice.bootstrap.UpdaterService;
import com.kaosmc.practice.bootstrap.annotation.Service;
import com.kaosmc.practice.common.logger.Logger;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.SettingsLocaleImpl;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Remi
 * @project kaos-practice
 * @date 24/07/2025
 */
@Service(provides = UpdaterService.class, priority = 1500)
public class UpdaterServiceImpl implements UpdaterService {
    private final LocaleService localeService;
    private final String currentVersion;

    private final String githubRepo = "RevereInc/kaos-practice";
    private String latestVersion;

    /**
     * DI Constructor for the UpdaterServiceImpl class.
     *
     * @param localeService The Locale service.
     */
    public UpdaterServiceImpl(LocaleService localeService) {
        this.localeService = localeService;
        this.currentVersion = KaosPractice.getInstance().getDescription().getVersion();
    }

    @Override
    public void setup(KaosContext context) {
        try {
            this.latestVersion = getLatestVersion();
        } catch (IOException e) {
            Logger.logException("Failed to fetch the latest version from GitHub", e);
        }
    }

    @Override
    public void initialize(KaosContext context) {
        this.checkForUpdates();
    }

    @Override
    public void checkForUpdates() {
        KaosPractice.getInstance().getServer().getScheduler().runTaskAsynchronously(KaosPractice.getInstance(), () -> {
            try {
                if (latestVersion != null && !currentVersion.equals(latestVersion)) {
                    Logger.warn("New version available: " + latestVersion + " (Current: " + currentVersion + ")");

                    if (this.shouldAutoUpdate()) {
                        this.downloadAndUpdate(latestVersion);
                    }
                }
            } catch (Exception e) {
                Logger.logException("Failed to check for updates", e);
            }
        });
    }

    @Override
    public void downloadAndUpdate(String version) {
        try {
            String downloadUrl = "https://github.com/" + githubRepo + "/releases/download/v" + version + "/Kaos-" + version + ".jar";

            URL url = new URL(downloadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            File pluginFile = new File(KaosPractice.getInstance().getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            File pluginDirectory = pluginFile.getParentFile();
            File updateDirectory = new File(pluginDirectory, "update");

            if (!updateDirectory.exists() && !updateDirectory.mkdirs()) {
                Logger.warn("Não foi possível criar a pasta de update: " + updateDirectory.getAbsolutePath());
                return;
            }

            File updateFile = new File(updateDirectory, pluginFile.getName());
            File tempFile = new File(updateDirectory, pluginFile.getName() + ".tmp");

            try (InputStream inputStream = connection.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(tempFile)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            if (updateFile.exists() && !updateFile.delete()) {
                Logger.warn("Falha ao remover atualização antiga em: " + updateFile.getAbsolutePath());
                return;
            }

            if (tempFile.renameTo(updateFile)) {
                Logger.info("Atualização " + version + " baixada em " + updateFile.getAbsolutePath() + ". Reinicie o servidor manualmente para aplicar.");
            } else {
                Logger.warn("Falha ao mover arquivo temporário para: " + updateFile.getName());
            }
        } catch (Exception e) {
            Logger.logException("Failed to download and update to version " + version, e);
        }
    }

    @Override
    public String getLatestVersion() throws IOException {
        URL url = new URL("https://api.github.com/repos/" + githubRepo + "/releases/latest");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

        try (InputStream inputStream = connection.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {

            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(inputStreamReader).getAsJsonObject();
            return json.get("tag_name").getAsString().replace("v", "");
        }
    }

    /**
     * Determines if the plugin should auto-update based on the configuration setting.
     *
     * @return True if auto-update is enabled, false otherwise.
     */
    private boolean shouldAutoUpdate() {
        return this.localeService.getBoolean(SettingsLocaleImpl.AUTO_UPDATE_BOOLEAN);
    }
}
