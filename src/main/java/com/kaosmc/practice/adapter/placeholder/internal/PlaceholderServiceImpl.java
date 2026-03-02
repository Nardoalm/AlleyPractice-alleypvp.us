package com.kaosmc.practice.adapter.placeholder.internal;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.adapter.placeholder.PlaceholderService;
import com.kaosmc.practice.bootstrap.AlleyContext;
import com.kaosmc.practice.bootstrap.annotation.Service;
import com.kaosmc.practice.common.logger.Logger;

/**
 * @author Emmy
 * @project alley-practice
 * @since 17/07/2025
 */
@Service(provides = PlaceholderService.class, priority = 430)
public class PlaceholderServiceImpl implements PlaceholderService {

    @Override
    public void initialize(AlleyContext context) {
        this.registerExpansion(context.getPlugin());
    }

    @Override
    public void registerExpansion(KaosPractice plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            Logger.info("PlaceholderAPI is not installed! Kaos Placeholder Expansion will not be registered.");
            return;
        }

        Logger.logTime(AlleyPlaceholderExpansion.class.getSimpleName(), () -> {
            AlleyPlaceholderExpansion expansion = new AlleyPlaceholderExpansion(plugin);
            expansion.register();
        });
    }
}