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
        UUID randomId = UUID.randomUUID();
        when(player.getUniqueId()).thenReturn(randomId);

        ability.setCooldown(player, 2000L);
        assertTrue(ability.hasCooldown(player), "O cooldown deveria estar ativo.");

        ability.setCooldown(player, 0L);
        assertFalse(ability.hasCooldown(player), "O cooldown deveria ter sido removido.");
    }

    @Test
    void shouldHandleNullPlayerSafely() {
        DummyAbility ability = new DummyAbility();

        assertFalse(ability.hasCooldown(null));
        ability.setCooldown(null, 1000L);
    }

    private static class DummyAbility extends Ability {
        private DummyAbility() {
            super("DUMMY_ABILITY");
        }

        @Override
        public String getName() {
            return "Dummy Ability";
        }
    }
}