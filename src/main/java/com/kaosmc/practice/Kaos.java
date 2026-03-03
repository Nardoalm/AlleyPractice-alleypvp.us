package com.kaosmc.practice;

import com.kaosmc.practice.feature.arena.Arena;
import com.kaosmc.practice.feature.arena.ArenaService;
import com.kaosmc.practice.feature.kit.KitService;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.data.ProfileData;
import com.kaosmc.practice.common.text.CC;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * KaosAPI – A central class providing easy access to Kaos.
 * <p>
 * This class allows other developers to interact with the server functionalities of Kaos,
 * such as registering custom code to be executed during bootstrap enable and disable,
 * and accessing player profiles.
 * </p>
 * <p>
 * Developers can use this class to easily hook into the lifecycle of the Kaos bootstrap
 * and retrieve player profiles without having to directly interact with other parts of the code.
 * </p>
 *
 * @author Emmy
 * @project Kaos
 * @since 22/04/2025
 */
@Getter
public class Kaos {

    @Getter
    private static Kaos instance;

    private final List<Runnable> onEnableCallbacks;
    private final List<Runnable> onDisableCallbacks;

    public Kaos() {
        instance = this;

        this.onEnableCallbacks = new ArrayList<>();
        this.onDisableCallbacks = new ArrayList<>();
    }

    /**
     * Register custom code to be executed when Kaos is enabled.
     * Developers can use this method to inject their code into the onEnable lifecycle of Kaos.
     *
     * @param callback The code to execute on enable.
     */
    public void registerOnEnableCallback(Runnable callback) {
        this.onEnableCallbacks.add(callback);
    }

    /**
     * Register custom code to be executed when Kaos is disabled.
     * Developers can use this method to inject their code into the onDisable lifecycle of Kaos.
     *
     * @param callback The code to execute on disable.
     */
    public void registerOnDisableCallback(Runnable callback) {
        this.onDisableCallbacks.add(callback);
    }

    /**
     * Run all registered onEnable callbacks.
     * This method executes each registered callback when Kaos is enabled.
     */
    public void runOnEnableCallbacks() {
        if (this.onEnableCallbacks.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&f[&6KaosAPI&f] Nenhum codigo externo registrado para executar no enable."));
            return;
        }

        for (Runnable callback : this.onEnableCallbacks) {
            callback.run();
        }
    }

    /**
     * Run all registered onDisable callbacks.
     * This method executes each registered callback when Kaos is disabled.
     */
    public void runOnDisableCallbacks() {
        if (this.onDisableCallbacks.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&f[&6KaosAPI&f] Nenhum codigo externo registrado para executar no disable."));
            return;
        }

        for (Runnable callback : this.onDisableCallbacks) {
            callback.run();
        }
    }

    /**
     * Get the profile of a player using their UUID.
     * Profile contains all types of non-statistic related data for the player.
     * Such as; UUID, Username, Join date, etc.
     *
     * @param uuid The UUID of the player to retrieve the profile for.
     * @return The profile associated with the UUID.
     */
    public Profile getProfile(UUID uuid) {
        return KaosPractice.getInstance().getService(ProfileService.class).getProfile(uuid);
    }

    /**
     * Get the profile data of a player using their UUID.
     * ProfileData contains all types of game related data for the player.
     * Such as; Ranked data, Unranked data, FFA data, Divisions, Titles, ELO, etc.
     *
     * @param uuid The UUID of the player to retrieve the profile data for.
     * @return The profile data associated with the UUID.
     */
    public ProfileData getProfileData(UUID uuid) {
        return KaosPractice.getInstance().getService(ProfileService.class).getProfile(uuid).getProfileData();
    }

    /**
     * Get a kit by its name.
     * This method retrieves a kit from the Kaos instance using its name.
     *
     * @param kitName The name of the kit to retrieve.
     * @return The Kit object associated with the given name, or null if not found.
     */
    public Kit getKit(String kitName) {
        return KaosPractice.getInstance().getService(KitService.class).getKits().stream().filter(kit -> kit.getName().equalsIgnoreCase(kitName)).findFirst().orElse(null);
    }

    /**
     * Get an arena by its name.
     * This method retrieves an arena from the Kaos instance using its name.
     *
     * @param arenaName The name of the arena to retrieve.
     * @return The AbstractArena object associated with the given name, or null if not found.
     */
    public Arena getArena(String arenaName) {
        return KaosPractice.getInstance().getService(ArenaService.class).getArenas().stream().filter(arena -> arena.getName().equalsIgnoreCase(arenaName)).findFirst().orElse(null);
    }

    /**
     * Get a random arena for a specific kit.
     * This method retrieves a random arena from the Kaos instance for the specified kit.
     *
     * @param kit The Kit object for which to retrieve a random arena.
     * @return A random AbstractArena object associated with the given kit.
     */
    public Arena getRandomArena(Kit kit) {
        return KaosPractice.getInstance().getService(ArenaService.class).getRandomArena(kit);
    }
}
