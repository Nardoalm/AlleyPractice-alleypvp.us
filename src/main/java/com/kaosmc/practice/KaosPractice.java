package com.kaosmc.practice;

import com.kaosmc.practice.bootstrap.KaosContext;
import com.kaosmc.practice.bootstrap.lifecycle.Service;
import com.kaosmc.practice.common.logger.Logger;
import com.kaosmc.practice.common.logger.PluginLogger;
import com.kaosmc.practice.core.database.task.RepositoryCleanupTask;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.VisualsLocaleImpl;
import com.kaosmc.practice.feature.cosmetic.task.CosmeticTask;
import com.kaosmc.practice.feature.match.task.other.ArrowRemovalTask;
import com.kaosmc.practice.feature.match.task.other.MatchPearlCooldownTask;
import com.kaosmc.practice.visual.tablist.task.TablistUpdateTask;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Kaos – A modern, modular Practice PvP core built from the ground up for Minecraft 1.8.
 * <p>
 * Developed by Revere Inc., Kaos focuses on clean, professional, and readable code,
 * making it easy for developers to jump into practice PvP development with minimal friction.
 * </p>
 *
 *
 * @author Emmy, Remi
 * @version 2.0 — Complete recode (entirely rewritten from scratch)
 * @see <a href="https://revere.dev">revere.dev</a>
 * @see <a href="https://discord.gg/revere">Discord Support</a>
 * @since 19/04/2024
 */
@Getter
public class KaosPractice extends JavaPlugin {
    @Getter
    private static KaosPractice instance;

    private final Kaos api;
    private KaosContext context;

    public static KaosPractice getInstance() {
        return instance;
    }

    public KaosPractice() {
        this.api = new Kaos();
    }

    @Override
    public void onEnable() {
        final long startTime = System.nanoTime();
        instance = this;

        this.validatePluginMetadata();

        try {
            this.context = new KaosContext(this);
            this.context.initialize();
        } catch (Exception exception) {
            Logger.logException("A fatal error occurred during service initialization. Kaos will be disabled.", exception);
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.scheduleTasks();

        final long durationMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        PluginLogger.onEnable(durationMillis);

        this.api.runOnEnableCallbacks();
    }

    @Override
    public void onDisable() {
        if (this.context != null) {
            this.context.shutdown();
        }

        PluginLogger.onDisable();

        this.api.runOnDisableCallbacks();
    }

    /**
     * Provides global, type-safe access to any managed service via its interface.
     *
     * @param serviceInterface The class of the service interface you want (e.g., ProfileService.class).
     * @return The service instance.
     * @throws IllegalStateException if the service is not found.
     */
    public <T extends Service> T getService(Class<T> serviceInterface) {
        Objects.requireNonNull(serviceInterface, "Service interface cannot be null");
        if (this.context == null) {
            throw new IllegalStateException("KaosContext is not available. The bootstrap may be disabling or failed to load.");
        }
        return this.context.getService(serviceInterface)
                .orElseThrow(() -> new IllegalStateException("Could not find a registered service for: " + serviceInterface.getSimpleName()));
    }

    private void validatePluginMetadata() {
        List<String> authors = this.getDescription().getAuthors();
        if (authors == null || authors.isEmpty()) {
            Logger.error("plugin.yml sem autores definidos. Verifique os metadados.");
        }
    }

    private void scheduleTasks() {
        final Map<String, Runnable> tasks = new LinkedHashMap<>();

        tasks.put(RepositoryCleanupTask.class.getSimpleName(), () -> new RepositoryCleanupTask(this).runTaskTimer(this, 0L, 40L));
        tasks.put(MatchPearlCooldownTask.class.getSimpleName(), () -> new MatchPearlCooldownTask().runTaskTimer(this, 2L, 2L));
        tasks.put(ArrowRemovalTask.class.getSimpleName(), () -> new ArrowRemovalTask().runTaskTimer(this, 20L, 20L));
        tasks.put(CosmeticTask.class.getSimpleName(), () -> new CosmeticTask(this).runTaskTimerAsynchronously(this, 0L, 4L));

        if (this.getService(LocaleService.class).getBoolean(VisualsLocaleImpl.TAB_LIST_ENABLED_BOOLEAN)) {
            tasks.put(TablistUpdateTask.class.getSimpleName(), () -> new TablistUpdateTask().runTaskTimer(this, 0L, 20L));
        }

        tasks.forEach(Logger::logTimeTask);
    }
}
