package us.alleypvp.practice.feature.hologram.manager;

import us.alleypvp.practice.bootstrap.lifecycle.Service; // Certifique-se de importar o Service correto do seu core
import us.alleypvp.practice.feature.hologram.leaderboard.KitLeaderboardHologram;
import us.alleypvp.practice.feature.leaderboard.LeaderboardType;
import org.bukkit.Location;

import java.util.List;

public interface HologramManager extends Service {

    void loadHolograms();

    void saveHologram(Location loc, String target, LeaderboardType type);

    boolean removeHologram(String target, LeaderboardType type);

    List<KitLeaderboardHologram> getActiveHolograms();
}