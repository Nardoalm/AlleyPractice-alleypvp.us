package us.alleypvp.practice.common.text;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.common.constants.PluginConstant;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 21/04/2025
 */
@UtilityClass
public class LoreHelper {
    /**
     * Returns a string representation of a boolean value indicating toggled status.
     *
     * @param value The boolean value to represent.
     * @return A formatted string indicating whether the feature is toggled or not.
     */
    public String displayToggled(boolean value) {
        String returnValue = value ? "&a&l✔ &bAtivado" : "&c&l✘ &cDesativado";
        return ChatColor.translateAlternateColorCodes('&', "&f&l│ " + returnValue);
    }
    /**
     * Returns a string representation of a boolean value.
     *
     * @param value The boolean value to represent.
     */
    public String displayEnabled(boolean value) {
        String returnValue = value ? "&bAtivado" : "&cDesativado";
        return ChatColor.translateAlternateColorCodes('&', "&f&l│ " + returnValue);
    }

    /**
     * Returns a string representation of a boolean value indicating visibility.
     *
     * @param value The boolean value to represent.
     */
    public String displayShown(boolean value) {
        String returnValue = value ? "&bExibido" : "&cOculto";
        return ChatColor.translateAlternateColorCodes('&', "&f&l│ " + returnValue);
    }

    /**
     * Returns a string representation of a boolean value indicating status.
     *
     * @param value The boolean value to represent.
     */
    public String displayStatus(boolean value) {
        String returnValue = value ? "&aAtivado" : "&cDesativado";
        return ChatColor.translateAlternateColorCodes('&', "&f● &bStatus: &f" + returnValue);
    }

    /**
     * Returns a string representation of a boolean value indicating a tick or cross.
     *
     * @param value The boolean value to represent.
     */
    public String displaySymbol(boolean value) {
        String returnValue = value ? "&a&l✔" : "&c&l✘";
        return ChatColor.translateAlternateColorCodes('&', "&f&l│ " + returnValue);
    }

    /**
     * Represents equipment selection lore for a player based on a permission.
     *
     * @param player        The player to check.
     * @param permission    The permission required to select.
     * @param inUse         Whether the item is in use or not.
     * @param clickToAction The action to perform when clicked.
     */
    public String selectionLoreWithPermission(Player player, String permission, boolean inUse, String clickToAction) {
        if (player.hasPermission(permission) && inUse) {
            return "&a&lSELECIONADO";
        } else if (player.hasPermission(permission) && !inUse) {
            return "&aClique para " + clickToAction + "!";
        } else {
            return AlleyPractice.getInstance().getService(PluginConstant.class).getPermissionLackMessage();
        }
    }

    /**
     * Represents equipment selection lore for a player.
     *
     * @param inUse         Whether the item is in use or not.
     * @param clickToAction The action to perform when clicked.
     */
    public String selectionLore(boolean inUse, String clickToAction) {
        if (inUse) {
            return "&aSelecionado.";
        } else {
            return "&aClique para " + clickToAction + ".";
        }
    }
}
