package com.kaosmc.practice.core.profile.progress.internal;

import com.kaosmc.practice.bootstrap.annotation.Service;
import com.kaosmc.practice.core.profile.progress.PlayerProgress;
import com.kaosmc.practice.core.profile.progress.ProgressService;
import com.kaosmc.practice.feature.division.Division;
import com.kaosmc.practice.feature.division.model.DivisionTier;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.data.ProfileData;
import com.kaosmc.practice.core.profile.data.types.ProfileUnrankedKitData;

import java.util.List;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
@Service(provides = ProgressService.class, priority = 410)
public class ProgressServiceImpl implements ProgressService {
    @Override
    public PlayerProgress calculateProgress(Profile profile, String kitName) {
        ProfileData profileData = profile.getProfileData();
        ProfileUnrankedKitData kitData = profileData.getUnrankedKitData().get(kitName);

        if (kitData == null) {
            return new PlayerProgress(0, 0, "N/A", true);
        }

        int wins = kitData.getWins();
        Division currentDivision = kitData.getDivision();
        DivisionTier currentTier = kitData.getTier();

        List<DivisionTier> tiers = currentDivision.getTiers();
        int tierIndex = tiers.indexOf(currentTier);

        int nextTierWins;
        boolean isMaxRank = false;

        if (tierIndex < tiers.size() - 1) {
            nextTierWins = tiers.get(tierIndex + 1).getRequiredWins();
        } else {
            Division nextDivision = profile.getNextDivision(kitName);
            if (nextDivision != null) {
                nextTierWins = nextDivision.getTiers().get(0).getRequiredWins();
            } else {
                nextTierWins = currentTier.getRequiredWins();
                isMaxRank = true;
            }
        }

        String nextRankName = isMaxRank ? "Max Rank" : profile.getNextDivisionAndTier(kitName);

        return new PlayerProgress(wins, nextTierWins, nextRankName, isMaxRank);
    }
}