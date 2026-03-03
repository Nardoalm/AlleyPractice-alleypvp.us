package com.kaosmc.practice.feature.arena.selection;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.item.ItemBuilder;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * @author Remi
 * @project Kaos
 * @date 5/20/2024
 */
@Data
@Getter
public class ArenaSelection {
    public static final ItemStack SELECTION_TOOL = new ItemBuilder(Material.FEATHER)
            .name("&6Selection Tool")
            .lore("&7Use this tool to select the minimum and maximum locations.")
            .build();

    private static final String SELECTION_METADATA = "kaos.selection";

    private Location minimum;
    private Location maximum;

    public ArenaSelection() {

    }

    /**
     * Creates a selection based on the player.
     *
     * @param player The player to create the selection for.
     * @return The selection created.
     */
    public static ArenaSelection createSelection(Player player) {
        if (player.hasMetadata(SELECTION_METADATA)) {
            return (ArenaSelection) player.getMetadata(SELECTION_METADATA).get(0).value();
        }

        ArenaSelection arenaSelection = new ArenaSelection();

        player.setMetadata(SELECTION_METADATA, new FixedMetadataValue(KaosPractice.getInstance(), arenaSelection));
        return arenaSelection;
    }

    /**
     * Gets the selection of the player.
     *
     * @param player The player to get the selection of.
     */
    public static void removeSelection(Player player) {
        if (player.hasMetadata(SELECTION_METADATA)) {
            player.removeMetadata(SELECTION_METADATA, KaosPractice.getInstance());
        }
    }

    /**
     * Checks if the player has a selection.
     *
     * @return True if the player has a selection, false otherwise.
     */
    public boolean hasSelection() {
        return minimum != null && maximum != null;
    }
}