package com.kaosmc.practice.common.serializer;

import com.kaosmc.practice.common.logger.Logger;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@UtilityClass
public class Serializer {
    /**
     * Serialize a location to a string.
     *
     * @param location The location to serialize.
     * @return The serialized location.
     */
    public String serializeLocation(Location location) {
        if (location == null) {
            return "null";
        }

        return location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() +
                ":" + location.getYaw() + ":" + location.getPitch();
    }

    /**
     * Deserialize a location from a string.
     *
     * @param source The source to deserialize.
     * @return The deserialized location.
     */
    public Location deserializeLocation(String source) {
        if (source == null || source.equalsIgnoreCase("null")) {
            return null;
        }

        String[] split = source.split(":");
        World world = Bukkit.getServer().getWorld(split[0]);

        if (world == null) {
            return null;
        }

        return new Location(world, Double.parseDouble(split[1]), Double.parseDouble(split[2]),
                Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
    }

    /**
     * Serialize an ItemStack array to a Base64 string
     *
     * @param items the ItemStack array to serialize
     * @return the serialized ItemStack array
     */
    public String serializeItemStack(ItemStack[] items) {
        try {
            ItemStack[] safeItems = items != null ? items : new ItemStack[0];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(safeItems.length);
            for (ItemStack item : safeItems) {
                dataOutput.writeObject(item);
            }

            dataOutput.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception exception) {
            throw new RuntimeException("Unable to serialize item stacks.", exception);
        }
    }

    /**
     * Deserialize an ItemStack array from a Base64 string
     *
     * @param data the Base64 string to deserialize
     * @return the deserialized ItemStack array
     */
    public ItemStack[] deserializeItemStack(String data) {
        if (data == null || data.trim().isEmpty()) {
            return new ItemStack[0];
        }

        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (Exception exception) {
            Logger.logException("Unable to deserialize item stacks.", exception);
            return new ItemStack[0];
        }
    }

    /**
     * Serialize a list of potion effects.
     *
     * @param potionEffects The potion effects.
     * @return The serialized potion effects.
     */
    public List<String> serializePotionEffects(List<PotionEffect> potionEffects) {
        return potionEffects.stream()
                .map(effect -> effect.getType().getName() + ":" + effect.getDuration() + ":" + effect.getAmplifier())
                .collect(Collectors.toList());
    }

    /**
     * Deserialize a list of potion effects.
     *
     * @param serializedEffects The serialized potion effects.
     * @return The potion effects.
     */
    public List<PotionEffect> deserializePotionEffects(List<String> serializedEffects) {
        return serializedEffects.stream()
                .map(s -> {
                    String[] parts = s.split(":");
                    if (parts.length < 3) return null;
                    PotionEffectType type = PotionEffectType.getByName(parts[0]);
                    int duration = Integer.parseInt(parts[1]);
                    int amplifier = Integer.parseInt(parts[2]);
                    return type != null ? new PotionEffect(type, duration, amplifier) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
