package us.alleypvp.practice.common.animation;

import us.alleypvp.practice.bootstrap.lifecycle.Service;
import us.alleypvp.practice.common.animation.internal.config.TextAnimation;
import us.alleypvp.practice.common.animation.internal.types.Animation;

import java.util.Set;

/**
 * @author Remi
 * @project kaos-practice
 * @date 2/07/2025
 */
public interface AnimationService extends Service {
    /**
     * Gets the set of all discovered internal animations.
     *
     * @return An unmodifiable set of internal animations.
     */
    Set<Animation> getInternalAnimations();

    /**
     * Gets the set of all discovered configuration-based text animations.
     *
     * @return An unmodifiable set of text animations.
     */
    Set<TextAnimation> getConfigAnimations();

    /**
     * Retrieves a specific animation instance by its class and type.
     *
     * @param clazz The class of the animation to retrieve.
     * @param type  The type of animation (INTERNAL or CONFIG).
     * @param <T>   The animation's type.
     * @return The requested animation instance.
     * @throws IllegalArgumentException if the animation is not found.
     */
    <T> T getAnimation(Class<T> clazz, AnimationType type);
}