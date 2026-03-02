package com.kaosmc.practice.common.reflect.internal.types;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.logger.Logger;
import com.kaosmc.practice.common.reflect.Reflection;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.VisualsLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Emmy
 * @project Kaos
 * @since 03/04/2025
 */
public class ActionBarReflectionServiceImpl implements Reflection {
    /**
     * Method to send an action bar message to a player in a specific interval.
     *
     * @param player          The player.
     * @param message         The message.
     * @param durationSeconds The duration to show the message (in seconds).
     */
    public void sendMessage(Player player, String message, int durationSeconds) {
        try {
            IChatBaseComponent chatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + CC.translate(message) + "\"}");
            PacketPlayOutChat packet = new PacketPlayOutChat(chatBaseComponent, (byte) 2);
            this.sendPacket(player, packet);

            if (durationSeconds > 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        IChatBaseComponent clearChatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"\"}");
                        PacketPlayOutChat clearPacket = new PacketPlayOutChat(clearChatBaseComponent, (byte) 2);
                        sendPacket(player, clearPacket);
                    }
                }.runTaskLater(KaosPractice.getInstance(), durationSeconds * 20L);
            }
        } catch (Exception exception) {
            Logger.logException("An error occurred while trying to send an action bar message to " + player.getName(), exception);
        }
    }

    /**
     * Method to send an action bar message to a player.
     *
     * @param player  The player to send the message to.
     * @param message The message to send.
     */
    public void sendMessage(Player player, String message) {
        try {
            IChatBaseComponent chatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + CC.translate(message) + "\"}");
            PacketPlayOutChat packet = new PacketPlayOutChat(chatBaseComponent, (byte) 2);
            this.sendPacket(player, packet);
        } catch (Exception exception) {
            Logger.logException("An error occurred while trying to send an action bar message to " + player.getName(), exception);
        }
    }

    /**
     * Sends a death message to the killer.
     *
     * @param killer The player who killed the victim.
     * @param victim The player who died.
     */
    public void sendDeathMessage(Player killer, Player victim) {
        LocaleService localeService = KaosPractice.getInstance().getService(LocaleService.class);

        if (localeService.getBoolean(VisualsLocaleImpl.ACTIONBAR_DEATH_MESSAGE_ENABLED_BOOLEAN)) {
            Profile victimProfile = KaosPractice.getInstance().getService(ProfileService.class).getProfile(victim.getUniqueId());
            Profile killerProfile = KaosPractice.getInstance().getService(ProfileService.class).getProfile(killer.getUniqueId());

            String deathMessage = localeService.getString(VisualsLocaleImpl.ACTIONBAR_DEATH_MESSAGE_FORMAT)
                    .replace("{victim}", victim.getName())
                    .replace("{killer}", killer.getName())
                    .replace("{name-color}", String.valueOf(victimProfile.getNameColor()))
                    .replace("{killer-name-color}", String.valueOf(killerProfile.getNameColor()));
            this.sendMessage(killer, deathMessage, 3);
        }
    }

    /**
     * Visualizes the target's health in the action bar for a player.
     *
     * @param player The player who will see the target's health.
     * @param target The player whose health will be visualized.
     */
    public void visualizeTargetHealth(Player player, Player target) {
        LocaleService localeService = KaosPractice.getInstance().getService(LocaleService.class);

        String message = localeService.getString(VisualsLocaleImpl.ACTIONBAR_HEALTH_INDICATOR_MESSAGE_FORMAT)
                .replace("{target}", target.getName())
                .replace("{name-color}", KaosPractice.getInstance().getService(ProfileService.class).getProfile(target.getUniqueId()).getNameColor().toString());

        String symbol = localeService.getString(VisualsLocaleImpl.ACTIONBAR_HEALTH_INDICATOR_SYMBOL_APPEARANCE);
        String fullColor = localeService.getString(VisualsLocaleImpl.ACTIONBAR_HEALTH_INDICATOR_SYMBOL_COLOR_FULL);
        String halfColor = localeService.getString(VisualsLocaleImpl.ACTIONBAR_HEALTH_INDICATOR_SYMBOL_COLOR_HALF);
        String emptyColor = localeService.getString(VisualsLocaleImpl.ACTIONBAR_HEALTH_INDICATOR_SYMBOL_COLOR_EMPTY);

        int maxHealth = (int) target.getMaxHealth() / 2;
        double rawHealth = target.getHealth() / 2;
        int currentHealth = (int) Math.ceil(rawHealth);

        StringBuilder healthBar = new StringBuilder();
        for (int i = 0; i < maxHealth; i++) {
            if (i < currentHealth) {
                healthBar.append(CC.translate(fullColor + symbol));
            } else if (i == currentHealth && rawHealth % 1 != 0) {
                healthBar.append(CC.translate(halfColor + symbol));
            } else {
                healthBar.append(CC.translate(emptyColor + symbol));
            }
        }

        String finalMessage = CC.translate(message.replace("{health-bar}", healthBar.toString()));
        this.sendMessage(player, finalMessage);
    }
}