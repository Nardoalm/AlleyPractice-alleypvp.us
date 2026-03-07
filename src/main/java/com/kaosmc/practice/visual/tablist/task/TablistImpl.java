package com.kaosmc.practice.visual.tablist.task;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.PlayerDisplayUtil;
import com.kaosmc.practice.common.PlaceholderUtil;
import com.kaosmc.practice.common.logger.Logger;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.VisualsLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.visual.tablist.TablistAdapter;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Kaos
 * @date 07/09/2024 - 15:16
 */
public class TablistImpl implements TablistAdapter {
    protected final KaosPractice plugin;

    /**
     * Constructor for the TablistVisualizer class.
     *
     * @param plugin The Kaos bootstrap instance.
     */
    public TablistImpl(KaosPractice plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> getHeader(Player player) {
        LocaleService localeService = this.plugin.getService(LocaleService.class);
        if (localeService == null) {
            return Collections.emptyList();
        }

        List<String> message = localeService.getStringList(VisualsLocaleImpl.TAB_LIST_HEADER);
        String displayName = PlayerDisplayUtil.resolveDisplayName(player, player.getName());
        message = message.stream()
                .map(line -> line.replace("{player}", displayName))
                .map(line -> line.replace("{online-players}", String.valueOf(this.plugin.getServer().getOnlinePlayers().size())))
                .map(line -> line.replace("{max-players}", String.valueOf(this.plugin.getServer().getMaxPlayers())))
                .map(line -> line.replace("{description}", this.plugin.getDescription().getDescription()))
                .collect(Collectors.toList());

        return PlaceholderUtil.setPapiSafe(player, message);
    }

    @Override
    public List<String> getFooter(Player player) {
        LocaleService localeService = this.plugin.getService(LocaleService.class);
        if (localeService == null) {
            return Collections.emptyList();
        }

        List<String> message = localeService.getStringList(VisualsLocaleImpl.TAB_LIST_FOOTER);
        String displayName = PlayerDisplayUtil.resolveDisplayName(player, player.getName());
        message = message.stream()
                .map(line -> line.replace("{player}", displayName))
                .map(line -> line.replace("{online-players}", String.valueOf(this.plugin.getServer().getOnlinePlayers().size())))
                .map(line -> line.replace("{max-players}", String.valueOf(this.plugin.getServer().getMaxPlayers())))
                .map(line -> line.replace("{description}", this.plugin.getDescription().getDescription()))
                .collect(Collectors.toList());

        return PlaceholderUtil.setPapiSafe(player, message);
    }

    @Override
    public void update(Player player) {
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        if (profileService == null) {
            return;
        }

        Profile profile = profileService.getProfile(player.getUniqueId());
        boolean tablistEnabled = profile != null
                && profile.getProfileData() != null
                && profile.getProfileData().getSettingData() != null
                && profile.getProfileData().getSettingData().isTablistEnabled();

        if (tablistEnabled) {
            List<String> headerLines = getHeader(player).stream()
                    .map(CC::translate)
                    .collect(Collectors.toList());

            List<String> footerLines = getFooter(player).stream()
                    .map(CC::translate)
                    .collect(Collectors.toList());

            String headerText = String.join("\n", headerLines);
            String footerText = String.join("\n", footerLines);

            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
            try {
                Field headerField = packet.getClass().getDeclaredField("a");
                headerField.setAccessible(true);
                headerField.set(packet, new ChatComponentText(headerText));

                Field footerField = packet.getClass().getDeclaredField("b");
                footerField.setAccessible(true);
                footerField.set(packet, new ChatComponentText(footerText));

                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            } catch (Exception e) {
                Logger.error("Failed to update tablist for " + player.getName());
            }
        } else {
            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
            try {
                Field headerField = packet.getClass().getDeclaredField("a");
                headerField.setAccessible(true);
                headerField.set(packet, new ChatComponentText(""));

                Field footerField = packet.getClass().getDeclaredField("b");
                footerField.setAccessible(true);
                footerField.set(packet, new ChatComponentText(""));

                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            } catch (Exception e) {
                Logger.error("Failed to update tablist for " + player.getName());
            }
        }
    }
}
