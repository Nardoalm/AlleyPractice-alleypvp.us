package com.kaosmc.practice.feature.cosmetic.task;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.PlayerUtil;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.feature.cosmetic.CosmeticListener;
import com.kaosmc.practice.feature.cosmetic.CosmeticService;
import com.kaosmc.practice.feature.cosmetic.internal.repository.CloakRepository;
import com.kaosmc.practice.feature.cosmetic.internal.repository.impl.cloak.BaseCloak;
import com.kaosmc.practice.feature.cosmetic.model.CosmeticType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Remi
 * @project kaos-practice
 * @date 4/08/2025
 */
public class CosmeticTask extends BukkitRunnable {

    private final KaosPractice plugin;
    private static final long CLOAK_STILL_DELAY = 1500;

    public CosmeticTask(KaosPractice plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        ProfileService profileService = plugin.getService(ProfileService.class);
        CosmeticService cosmeticService = plugin.getService(CosmeticService.class);
        if (profileService == null || cosmeticService == null) return;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (CosmeticListener.isPlayerStill(player, CLOAK_STILL_DELAY) && PlayerUtil.inLobby(player)) {
                Profile profile = profileService.getProfile(player.getUniqueId());
                if (profile == null) continue;

                String cloakName = profile.getProfileData().getCosmeticData().getSelected(CosmeticType.CLOAK);
                if (cloakName == null || cloakName.equalsIgnoreCase("None")) continue;

                CloakRepository repo = cosmeticService.getRepository(CosmeticType.CLOAK, CloakRepository.class);
                if (repo == null) continue;

                BaseCloak cloak = repo.getCosmetic(cloakName);
                if (cloak != null) {
                    cloak.render(player);
                }
            }
        }
    }
}