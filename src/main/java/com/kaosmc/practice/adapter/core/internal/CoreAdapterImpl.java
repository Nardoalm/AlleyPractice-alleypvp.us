package com.kaosmc.practice.adapter.core.internal;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.adapter.core.Core;
import com.kaosmc.practice.adapter.core.CoreAdapter;
import com.kaosmc.practice.adapter.core.CoreType;
import com.kaosmc.practice.bootstrap.KaosContext;
import com.kaosmc.practice.bootstrap.annotation.Service;
import lombok.Getter;
import me.activated.core.plugin.AquaCoreAPI;
import services.plasma.helium.api.HeliumAPI;
import xyz.refinedev.phoenix.SharedAPI;

/**
 * @author Emmy
 * @project Kaos
 * @since 26/04/2025
 */
@Getter
@Service(provides = CoreAdapter.class, priority = 60)
public class CoreAdapterImpl implements CoreAdapter {

    private final KaosPractice plugin;
    private Core core;

    /**
     * Constructor for DI. Receives the main bootstrap instance.
     */
    public CoreAdapterImpl(KaosPractice plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize(KaosContext context) {
        this.core = this.determineCore();
    }

    @Override
    public Core getCore() {
        if (this.core == null) {
            throw new IllegalStateException("CoreAdapter has not been initialized yet.");
        }
        return this.core;
    }

    /**
     * Determines the server implementation to use based on the enabled plugins.
     *
     * @return The selected server implementation.
     */
    private Core determineCore() {
        Core selectedCore = new DefaultCoreImpl(this.plugin);

        for (CoreType coreType : CoreType.values()) {
            if (this.plugin.getServer().getPluginManager().isPluginEnabled(coreType.getPluginName())) {
                switch (coreType) {
                    case PHOENIX:
                        selectedCore = new PhoenixCoreImpl(SharedAPI.getInstance());
                        break;
                    case AQUA:
                        selectedCore = new AquaCoreImpl(AquaCoreAPI.INSTANCE, this.plugin);
                        break;
                    case HELIUM:
                        selectedCore = new HeliumCoreImpl(HeliumAPI.INSTANCE);
                        break;
                }
                break;
            }
        }
        return selectedCore;
    }
}