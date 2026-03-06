package com.kaosmc.practice.feature.abilities;

import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AbilityCooldownTest {

    @Test
    void shouldSetAndRemoveCooldownWithoutNpe() {
        DummyAbility ability = new DummyAbility();
        Player player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());

        ability.setCooldown(player, 2_000L);
        assertTrue(ability.hasCooldown(player));

        ability.setCooldown(player, 0L);
        assertFalse(ability.hasCooldown(player));
    }

    @Test
    void shouldHandleNullPlayerSafely() {
        DummyAbility ability = new DummyAbility();

        assertFalse(ability.hasCooldown(null));
        ability.setCooldown(null, 1_000L);
    }

    private static class DummyAbility extends Ability {
        private DummyAbility() {
            super("DUMMY_ABILITY");
        }
    }
}
