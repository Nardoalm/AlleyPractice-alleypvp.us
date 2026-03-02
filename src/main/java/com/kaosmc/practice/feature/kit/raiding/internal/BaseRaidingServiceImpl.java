package com.kaosmc.practice.feature.kit.raiding.internal;

import com.kaosmc.practice.bootstrap.AlleyContext;
import com.kaosmc.practice.bootstrap.annotation.Service;
import com.kaosmc.practice.common.logger.Logger;
import com.kaosmc.practice.core.config.ConfigService;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.kit.KitService;
import com.kaosmc.practice.feature.kit.raiding.BaseRaidingService;
import com.kaosmc.practice.feature.kit.setting.types.mode.KitSettingRaiding;
import com.kaosmc.practice.feature.match.model.BaseRaiderRole;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project alley-practice
 * @date 20/06/2025
 */
@Getter
@Service(provides = BaseRaidingService.class, priority = 100)
public class BaseRaidingServiceImpl implements BaseRaidingService {
    private final KitService kitService;
    private final ConfigService configService;

    private final Map<String, Map<BaseRaiderRole, Kit>> raidingKitMappings = new HashMap<>();

    /**
     * Constructor for DI.
     */
    public BaseRaidingServiceImpl(KitService kitService, ConfigService configService) {
        this.kitService = kitService;
        this.configService = configService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.loadRaidingKitMappings();
    }

    public void loadRaidingKitMappings() {
        this.raidingKitMappings.clear();
        FileConfiguration config = this.configService.getKitsConfig();
        ConfigurationSection kitsSection = config.getConfigurationSection("kits");
        if (kitsSection == null) {
            Logger.warn("Could not find 'kits' section in kits.yml for RaidingService.");
            return;
        }

        for (Kit kit : this.kitService.getKits()) {
            if (kit.isSettingEnabled(KitSettingRaiding.class)) {
                String key = "kits." + kit.getName();
                this.loadRaidingKitMapping(kit, config, key);
            }
        }
    }

    private void loadRaidingKitMapping(Kit parentKit, FileConfiguration config, String key) {
        ConfigurationSection raidingSection = config.getConfigurationSection(key + ".raiding-role-kits");
        if (raidingSection == null) {
            return;
        }

        Map<BaseRaiderRole, Kit> roleKits = new HashMap<>();
        for (String roleName : raidingSection.getKeys(false)) {
            try {
                BaseRaiderRole role = BaseRaiderRole.valueOf(roleName.toUpperCase());
                String roleKitName = raidingSection.getString(roleName);

                Kit roleKit = this.kitService.getKit(roleKitName);

                if (roleKit != null) {
                    roleKits.put(role, roleKit);
                    roleKit.setEnabled(false);
                } else {
                    Logger.error("Raiding sub-kit for role " + role + " not found: " + roleKitName);
                }
            } catch (IllegalArgumentException e) {
                Logger.error("Invalid raiding role: '" + roleName + "' in parent kit: " + parentKit.getName());
            }
        }

        if (!roleKits.isEmpty()) {
            this.raidingKitMappings.put(parentKit.getName(), roleKits);
        }
    }

    @Override
    public void setRaidingKitMapping(Kit parentKit, BaseRaiderRole role, Kit roleKit) {
        this.raidingKitMappings.computeIfAbsent(parentKit.getName(), k -> new HashMap<>()).put(role, roleKit);

        FileConfiguration config = this.configService.getKitsConfig();
        String key = "kits." + parentKit.getName() + ".raiding-role-kits." + role.name().toLowerCase();
        config.set(key, roleKit.getName());

        this.kitService.saveKit(parentKit);
    }

    @Override
    public void removeRaidingKitMapping(Kit parentKit, BaseRaiderRole role) {
        Map<BaseRaiderRole, Kit> roleKits = this.raidingKitMappings.get(parentKit.getName());
        if (roleKits == null || !roleKits.containsKey(role)) {
            return;
        }

        roleKits.remove(role);
        if (roleKits.isEmpty()) {
            this.raidingKitMappings.remove(parentKit.getName());
        }

        FileConfiguration config = this.configService.getKitsConfig();
        String key = "kits." + parentKit.getName() + ".raiding-role-kits." + role.name().toLowerCase();
        config.set(key, null);

        this.kitService.saveKit(parentKit);
    }

    @Override
    public Kit getRaidingKitByRole(Kit parentKit, BaseRaiderRole role) {
        return this.raidingKitMappings.getOrDefault(parentKit.getName(), new HashMap<>()).get(role);
    }

    @Override
    public Kit getRaidingKit() {
        return this.kitService.getKits().stream()
                .filter(kit -> kit.isSettingEnabled(KitSettingRaiding.class))
                .findFirst()
                .orElse(null);
    }

    public Kit getRaidingKitByRole(BaseRaiderRole role) {
        Kit primaryKit = this.getRaidingKit();
        if (primaryKit == null) {
            return null;
        }
        return this.getRaidingKitByRole(primaryKit, role);
    }

    @Override
    public boolean hasRaidingKit(Kit kit) {
        return this.raidingKitMappings.containsKey(kit.getName());
    }
}