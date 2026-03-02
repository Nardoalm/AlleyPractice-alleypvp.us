package com.kaosmc.practice.visual.tablist.task;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.visual.tablist.TablistAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Emmy
 * @project Kaos
 * @date 07/09/2024 - 15:23
 */
public class TablistUpdateTask extends BukkitRunnable {
    protected final TablistAdapter tablistAdapterVisualizer;

    public TablistUpdateTask() {
        this.tablistAdapterVisualizer = new TablistImpl(KaosPractice.getInstance());
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.tablistAdapterVisualizer.update(player);
        }
    }
}