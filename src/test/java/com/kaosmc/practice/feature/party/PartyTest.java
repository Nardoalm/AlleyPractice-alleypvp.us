package com.kaosmc.practice.feature.party;

import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PartyTest {

    @Test
    void shouldIdentifyLeaderUsingUuidNotReference() {
        UUID leaderUuid = UUID.randomUUID();

        Player leader = mock(Player.class);
        when(leader.getUniqueId()).thenReturn(leaderUuid);

        Party party = new Party(leader);

        Player sameLeaderDifferentReference = mock(Player.class);
        when(sameLeaderDifferentReference.getUniqueId()).thenReturn(leaderUuid);

        assertTrue(party.isLeader(sameLeaderDifferentReference));
    }

    @Test
    void shouldReturnFalseWhenPlayerIsNull() {
        UUID leaderUuid = UUID.randomUUID();
        Player leader = mock(Player.class);
        when(leader.getUniqueId()).thenReturn(leaderUuid);

        Party party = new Party(leader);
        assertFalse(party.isLeader(null));
    }

    @Test
    void shouldTrackDefaultMaxSizeAndFullState() {
        UUID leaderUuid = UUID.randomUUID();
        Player leader = mock(Player.class);
        when(leader.getUniqueId()).thenReturn(leaderUuid);

        Party party = new Party(leader);

        assertEquals(Party.DEFAULT_MAX_SIZE, party.getMaxSize());
        assertFalse(party.isFull());

        for (int index = 1; index < Party.DEFAULT_MAX_SIZE; index++) {
            party.getMembers().add(UUID.randomUUID());
        }

        assertTrue(party.isFull());
    }
}
