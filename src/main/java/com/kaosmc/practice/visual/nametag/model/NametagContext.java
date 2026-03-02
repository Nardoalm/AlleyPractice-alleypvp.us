package com.kaosmc.practice.visual.nametag.model;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
@Getter
public class NametagContext {
    private final Profile viewerProfile;
    private final Profile targetProfile;
    private final Player viewer;
    private final Player target;

    public NametagContext(Player viewer, Player target) {
        this.viewer = viewer;
        this.target = target;

        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        this.viewerProfile = profileService.getProfile(viewer.getUniqueId());
        this.targetProfile = profileService.getProfile(target.getUniqueId());
    }
}