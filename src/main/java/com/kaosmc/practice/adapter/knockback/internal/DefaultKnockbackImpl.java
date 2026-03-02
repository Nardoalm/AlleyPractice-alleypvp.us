package com.kaosmc.practice.adapter.knockback.internal;

import com.kaosmc.practice.adapter.knockback.Knockback;
import com.kaosmc.practice.adapter.knockback.KnockbackType;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @since 29/06/2025
 */
public class DefaultKnockbackImpl implements Knockback {
    @Override
    public KnockbackType getType() {
        return KnockbackType.DEFAULT;
    }

    @Override
    public void applyKnockback(Player player, String profile) {

    }
}