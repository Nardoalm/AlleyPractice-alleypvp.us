package com.kaosmc.practice.adapter.placeholder.internal;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.constants.PluginConstant;
import com.kaosmc.practice.feature.level.LevelService;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.data.ProfileData;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.common.animation.internal.types.DotAnimation;
import com.kaosmc.practice.feature.level.data.LevelData;
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
        this.notAvailableString = CC.translate("&cN/A");
    }

    @Override
    public @NotNull String getIdentifier() {
        // Usando o nome definido nas constantes (ex: kaos)
        PluginConstant constant = this.plugin.getService(PluginConstant.class);
        return constant != null ? constant.getName().toLowerCase() : "kaos";
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

        // --- Animações Globais (Independente de Perfil) ---
        if (params.equalsIgnoreCase("dot-animation")) {
            return dotAnimation.getCurrentFrame();
        }

        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        Profile profile = (profileService != null) ? profileService.getProfile(player.getUniqueId()) : null;

        if (profile == null || profile.getProfileData() == null) {
            return this.notAvailableString;
        }

        ProfileData profileData = profile.getProfileData();

        // --- Placeholders com Parâmetros Dinâmicos ---
        if (params.startsWith("division_")) {
            String kitName = params.substring("division_".length());
            if (profileData.getUnrankedKitData().containsKey(kitName)) {
                String division = profileData.getUnrankedKitData().get(kitName).getDivision().getName();
                return division != null ? CC.translate(division) : this.notAvailableString;
            }
            return this.notAvailableString;
        }

        // --- Placeholders Fixas ---
        switch (params.toLowerCase()) {
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
                LevelService levelService = this.plugin.getService(LevelService.class);
                if (levelService != null) {
                    int level = Integer.parseInt(profileData.getGlobalLevel());
                    LevelData levelData = levelService.getLevel(level);
                    if (levelData != null && levelData.getDisplayName() != null) {
                        return CC.translate(levelData.getDisplayName());
                    }
                    return String.valueOf(level);
                }
                return "0";

            case "player-coins":
                return String.valueOf(profileData.getCoins());

            default:
                return null;
        }
    }
}