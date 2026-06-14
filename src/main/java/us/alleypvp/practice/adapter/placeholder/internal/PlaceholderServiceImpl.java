package us.alleypvp.practice.adapter.placeholder.internal;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.adapter.placeholder.PlaceholderService;
import us.alleypvp.practice.bootstrap.KaosContext;
import us.alleypvp.practice.bootstrap.annotation.Service;
import us.alleypvp.practice.common.logger.Logger;

/**
 * @author Emmy
 * @project kaos-practice
 * @since 17/07/2025
 */
@Service(provides = PlaceholderService.class, priority = 430)
public class PlaceholderServiceImpl implements PlaceholderService {

    @Override
    public void initialize(KaosContext context) {
        this.registerExpansion(context.getPlugin());
    }

    @Override
    public void registerExpansion(AlleyPractice plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            Logger.info("PlaceholderAPI is not installed! Alley Placeholder Expansion will not be registered.");
            return;
        }
        Logger.logTime(KaosPlaceholderExpansion.class.getSimpleName(), () -> {
            KaosPlaceholderExpansion expansion = new KaosPlaceholderExpansion(plugin);
            expansion.register();
        });
    }
}
