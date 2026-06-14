package us.alleypvp.practice.feature.cosmetic.task;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.common.PlayerUtil;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.feature.cosmetic.CosmeticListener;
import us.alleypvp.practice.feature.cosmetic.CosmeticService;
import us.alleypvp.practice.feature.cosmetic.internal.repository.CloakRepository;
import us.alleypvp.practice.feature.cosmetic.internal.repository.impl.cloak.BaseCloak;
import us.alleypvp.practice.feature.cosmetic.model.CosmeticType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Remi
 * @project kaos-practice
 * @date 4/08/2025
 */
public class CosmeticTask extends BukkitRunnable {

    private final AlleyPractice plugin;
    private static final long CLOAK_STILL_DELAY = 1500;

    public CosmeticTask(AlleyPractice plugin) {
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