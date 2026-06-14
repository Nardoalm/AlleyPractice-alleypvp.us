package us.alleypvp.practice.adapter.knockback.internal;

import us.alleypvp.practice.adapter.knockback.Knockback;
import us.alleypvp.practice.adapter.knockback.KnockbackType;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project kaos-practice
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