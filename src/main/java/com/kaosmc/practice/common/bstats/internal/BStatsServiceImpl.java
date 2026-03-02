package com.kaosmc.practice.common.bstats.internal;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.bootstrap.annotation.Service;
import com.kaosmc.practice.common.bstats.BStatsService;
import org.bstats.bukkit.Metrics;
import com.kaosmc.practice.bootstrap.AlleyContext;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/07/2025
 */
@Service(provides = BStatsService.class, priority = 1)
public class BStatsServiceImpl implements BStatsService {
    @Override
    public void setup(AlleyContext context) {
        Metrics metrics = new Metrics(KaosPractice.getInstance(), 26678);
    }
}
