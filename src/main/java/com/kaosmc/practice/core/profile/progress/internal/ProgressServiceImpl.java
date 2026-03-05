package com.kaosmc.practice.core.profile.progress.internal;

import com.kaosmc.practice.bootstrap.annotation.Service;
import com.kaosmc.practice.feature.division.Division;
import com.kaosmc.practice.feature.division.model.DivisionTier;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.data.ProfileData;
import com.kaosmc.practice.core.profile.data.types.ProfileUnrankedKitData;
import com.kaosmc.practice.core.profile.progress.PlayerProgress;
import com.kaosmc.practice.core.profile.progress.ProgressService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Remi
 * @project kaos-practice
 * @date 2/07/2025
 */
@Service(provides = ProgressService.class, priority = 410)
public class ProgressServiceImpl implements ProgressService {
    private static final String FALLBACK_NEXT_RANK = "N/A";

    @Override
    public PlayerProgress calculateProgress(Profile profile, String kitName) {
        if (profile == null || kitName == null || kitName.trim().isEmpty()) {
            return createFallbackProgress(0);
        }

        ProfileData profileData = profile.getProfileData();
        if (profileData == null || profileData.getUnrankedKitData() == null) {
            return createFallbackProgress(0);
        }

        ProfileUnrankedKitData kitData = profileData.getUnrankedKitData().get(kitName);

        if (kitData == null) {
            return createFallbackProgress(0);
        }

        int wins = Math.max(0, kitData.getWins());
        Division currentDivision = kitData.getDivision();

        if (currentDivision == null) {
            return createFallbackProgress(wins);
        }

        DivisionTier currentTier = null;
        try {
            currentTier = kitData.getTier();
        } catch (Exception ignored) {
            // Invalid division/tier state in profile data; fall back to wins-based resolution below.
        }

        List<DivisionTier> tiers = currentDivision.getTiers();
        if (tiers == null || tiers.isEmpty()) {
            return createFallbackProgress(wins);
        }

        int tierIndex = resolveTierIndex(tiers, currentTier, wins);
        DivisionTier currentResolvedTier = tiers.get(tierIndex);

        int nextTierWins;
        String nextRankName;
        boolean isMaxRank;

        if (tierIndex < tiers.size() - 1) {
            DivisionTier nextTier = tiers.get(tierIndex + 1);
            nextTierWins = Math.max(nextTier.getRequiredWins(), wins);
            nextRankName = formatRankName(currentDivision, nextTier);
            isMaxRank = false;
        } else {
            Division nextDivision = null;
            try {
                nextDivision = profile.getNextDivision(kitName);
            } catch (Exception ignored) {
                // Profile has incomplete division mapping; handle as max rank below.
            }
            DivisionTier nextDivisionFirstTier = getFirstTier(nextDivision);
            if (nextDivisionFirstTier != null) {
                nextTierWins = Math.max(nextDivisionFirstTier.getRequiredWins(), wins);
                nextRankName = formatRankName(nextDivision, nextDivisionFirstTier);
                isMaxRank = false;
            } else {
                nextTierWins = Math.max(currentResolvedTier.getRequiredWins(), wins);
                nextRankName = "Max Rank";
                isMaxRank = true;
            }
        }

        return new PlayerProgress(wins, nextTierWins, nextRankName, isMaxRank);
    }

    private PlayerProgress createFallbackProgress(int wins) {
        int safeWins = Math.max(0, wins);
        int fallbackNextTierWins = Math.max(1, safeWins + 1);
        return new PlayerProgress(safeWins, fallbackNextTierWins, FALLBACK_NEXT_RANK, false);
    }

    private DivisionTier getFirstTier(Division division) {
        if (division == null || division.getTiers() == null || division.getTiers().isEmpty()) {
            return null;
        }
        return division.getTiers().get(0);
    }

    private int resolveTierIndex(List<DivisionTier> tiers, DivisionTier currentTier, int wins) {
        int directIndex = tiers.indexOf(currentTier);
        if (directIndex >= 0) {
            return directIndex;
        }

        int bestIndex = 0;
        List<DivisionTier> safeTiers = tiers == null ? Collections.emptyList() : tiers;
        for (int i = 0; i < safeTiers.size(); i++) {
            DivisionTier tier = safeTiers.get(i);
            if (tier != null && wins >= tier.getRequiredWins()) {
                bestIndex = i;
            }
        }
        return bestIndex;
    }

    private String formatRankName(Division division, DivisionTier tier) {
        if (division == null || tier == null) {
            return FALLBACK_NEXT_RANK;
        }

        String divisionName = Objects.toString(division.getName(), "").trim();
        String tierName = Objects.toString(tier.getName(), "").trim();

        if (divisionName.isEmpty() && tierName.isEmpty()) {
            return FALLBACK_NEXT_RANK;
        }
        if (divisionName.isEmpty()) {
            return tierName;
        }
        if (tierName.isEmpty()) {
            return divisionName;
        }
        return divisionName + " " + tierName;
    }
}
