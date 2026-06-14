package us.alleypvp.practice.feature.leaderboard.internal;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.feature.hologram.manager.HologramManager;
import us.alleypvp.practice.feature.hologram.leaderboard.KitLeaderboardHologram;
import org.bukkit.scheduler.BukkitRunnable;

public class LeaderboardUpdateTask extends BukkitRunnable {

    @Override
    public void run() {
        HologramManager manager = AlleyPractice.getInstance().getService(HologramManager.class);
        if (manager == null) return;

        for (KitLeaderboardHologram holo : manager.getActiveHolograms()) {
            holo.update();
        }
    }
}