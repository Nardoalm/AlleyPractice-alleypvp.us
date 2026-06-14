package us.alleypvp.practice.feature.match.snapshot.menu.button.items;

import us.alleypvp.practice.library.menu.Button;
import us.alleypvp.practice.feature.match.snapshot.Snapshot;
import us.alleypvp.practice.common.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 07/10/2024
 */
@AllArgsConstructor
public class SnapshotArmorButton extends Button {
    private final Snapshot snapshot;
    private int armorPart;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.snapshot.getArmor()[this.armorPart]).build();
    }
}