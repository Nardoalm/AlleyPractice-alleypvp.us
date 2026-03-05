package com.kaosmc.practice.common.placeholder;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.common.animation.internal.types.DotAnimation;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class KaosPlaceholderExpansion extends PlaceholderExpansion {

    private final KaosPractice plugin;
    private final DotAnimation dotAnimation = new DotAnimation(); // Instância ÚNICA para não travar no "."

    public KaosPlaceholderExpansion(KaosPractice plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "kaos"; // Isso define o prefixo %kaos_...%
    }

    @Override
    public @NotNull String getAuthor() {
        return "Emmy";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; // Mantém registrado mesmo após /papi reload
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return "";

        ProfileService profileService = plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        // %kaos_level%
        if (params.equalsIgnoreCase("level")) {
            if (profile != null && profile.getProfileData() != null) {
                return String.valueOf(profile.getProfileData().getGlobalLevel());
            }
            return "0";
        }

        // %kaos_dot_animation% ou %kaos_dot-animation%
        if (params.equalsIgnoreCase("dot-animation") || params.equalsIgnoreCase("dot_animation")) {
            return dotAnimation.getCurrentFrame();
        }

        return null;
    }
}