package us.alleypvp.practice.feature.arena.internal.types;

import us.alleypvp.practice.feature.arena.ArenaType;
import org.bukkit.Location;

public class EventArena extends SharedArena {
    public EventArena(String name, Location minimum, Location maximum) {
        super(name, minimum, maximum);
    }

    @Override
    public ArenaType getType() {
        return ArenaType.EVENT;
    }
}
