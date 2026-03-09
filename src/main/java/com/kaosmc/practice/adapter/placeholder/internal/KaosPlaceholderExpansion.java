package com.kaosmc.practice.adapter.placeholder.internal;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.constants.PluginConstant;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.data.ProfileData;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.common.animation.internal.types.DotAnimation;
import com.kaosmc.practice.common.text.LevelBadgeUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Emmy
 * @project Kaos
 * @since 21/05/2025
 */
public class KaosPlaceholderExpansion extends PlaceholderExpansion {

    protected final KaosPractice plugin;
    protected final String notAvailableString;

    // Instância única para manter o tempo da animação correto (não reseta o frame)
    private final DotAnimation dotAnimation = new DotAnimation();

    /**
     * Constructor for the KaosPlaceholderExpansion class.
     *
     * @param plugin The Kaos bootstrap instance.
     */
    public KaosPlaceholderExpansion(KaosPractice plugin) {
        this.plugin = plugin;
        this.notAvailableString = CC.translate("&cN/D");
    }

    @Override
    public @NotNull String getIdentifier() {
        // Keep a stable namespace for PlaceholderAPI calls such as %kaos_level%.
        return "kaos";
    }

    @Override
    public @NotNull String getAuthor() {
        return this.plugin.getDescription().getAuthors().isEmpty() ? "Emmy" : this.plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        PluginConstant constant = this.plugin.getService(PluginConstant.class);
        return constant != null ? constant.getVersion() : "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        String normalizedParams = params.toLowerCase().replace('_', '-');

        // --- Animações Globais (Independente de Perfil) ---
        if (normalizedParams.equals("dot-animation")) {
            return dotAnimation.getCurrentFrame();
        }

        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = (profileService != null) ? profileService.getProfile(player.getUniqueId()) : null;

        if (profile == null || profile.getProfileData() == null) {
            return this.notAvailableString;
        }

        ProfileData profileData = profile.getProfileData();

        // --- Placeholders com Parâmetros Dinâmicos ---
        if (normalizedParams.startsWith("division-")) {
            String kitName = params.length() > 9 ? params.substring(9) : "";
            if (kitName.trim().isEmpty()) {
                return this.notAvailableString;
            }
            String resolvedKitName = profileData.getUnrankedKitData().keySet().stream()
                    .filter(key -> key.equalsIgnoreCase(kitName))
                    .findFirst()
                    .orElse(null);

            if (resolvedKitName != null) {
                if (profileData.getUnrankedKitData().get(resolvedKitName).getDivision() == null) {
                    return this.notAvailableString;
                }
                String division = profileData.getUnrankedKitData().get(resolvedKitName).getDivision().getName();
                return division != null ? CC.translate(division) : this.notAvailableString;
            }
            return this.notAvailableString;
        }

        // --- Placeholders Fixas ---
        switch (normalizedParams) {
            case "player-global-elo":
                return String.valueOf(profileData.getElo());

            case "player-unranked-wins":
                return String.valueOf(profileData.getTotalWins());

            case "player-unranked-losses":
                return String.valueOf(profileData.getTotalLosses());

            case "player-ranked-wins":
                return String.valueOf(profileData.getRankedWins());

            case "player-ranked-losses":
                return String.valueOf(profileData.getRankedLosses());

            case "player-level":
            case "level":
            case "nivel":
            case "nível":
                return LevelBadgeUtil.getDisplayBadge(player, profileData.getExperience());

            case "player-level-number":
            case "level-number":
            case "nivel-numero":
                return String.valueOf(LevelBadgeUtil.getDisplayLevel(player, profileData.getExperience()));

            case "player-experience":
            case "xp":
                return String.valueOf(profileData.getExperience());

            case "level-progress":
            case "player-level-progress":
                return LevelBadgeUtil.getProgressDetails(profileData.getExperience());

            case "player-coins":
                return String.valueOf(profileData.getCoins());

            default:
                return null;
        }
    }
}
