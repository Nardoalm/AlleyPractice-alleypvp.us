package us.alleypvp.practice.feature.match.snapshot.menu.button.items;

import us.alleypvp.practice.library.menu.Button;
import us.alleypvp.practice.common.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @since 27/06/2025
 */
@AllArgsConstructor
public class SnapshotInventoryButton extends Button {
    private final ItemStack item;

    @Override
    public ItemStack getButtonItem(Player player) {
        if (this.item == null) {
            return new ItemStack(Material.AIR);
        }
        return new ItemBuilder(this.item).build();
    }
}
