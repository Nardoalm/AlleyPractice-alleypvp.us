package com.kaosmc.practice.adapter.core.listener;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.adapter.core.Core;
import com.kaosmc.practice.adapter.core.CoreAdapter;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.SettingsLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.feature.filter.FilterService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author Emmy
 * @project Kaos
 * @since 26/04/2025
 */
public class CoreChatListener implements Listener {
    protected final KaosPractice plugin = KaosPractice.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        Core core = null;
        try {
            CoreAdapter coreAdapter = this.plugin.getService(CoreAdapter.class);
            if (coreAdapter != null) {
                core = coreAdapter.getCore();
            }
        } catch (Exception ignored) {
            core = null;
        }

        if (core == null) {
            return;
        }

        FilterService filterService = this.plugin.getService(FilterService.class);
        if (filterService == null) {
            return;
        }

        String eventMessage = event.getMessage();

        if (filterService.isProfanity(eventMessage)) {
            filterService.notifyStaff(eventMessage, player);
        }

        LocaleService localeService = this.plugin.getService(LocaleService.class);
        if (localeService == null) {
            return;
        }

        if (localeService.getBoolean(SettingsLocaleImpl.SERVER_CHAT_FORMAT_ENABLED_BOOLEAN)) {
            String separator = localeService.getString(SettingsLocaleImpl.SERVER_CHAT_FORMAT_SEPARATOR);
            String format = core.getChatFormat(player, eventMessage, CC.translate(separator));
            String censoredFormat = core.getChatFormat(player, filterService.censorWords(eventMessage), CC.translate(separator));

            for (Player recipient : event.getRecipients()) {
                boolean profanityFilterEnabled = false;
                try {
                    Profile profile = this.plugin.getService(ProfileService.class).getProfile(recipient.getUniqueId());
                    profanityFilterEnabled = profile != null
                            && profile.getProfileData() != null
                            && profile.getProfileData().getSettingData() != null
                            && profile.getProfileData().getSettingData().isProfanityFilterEnabled();
                } catch (Exception ignored) {
                    profanityFilterEnabled = false;
                }

                if (profanityFilterEnabled) {
                    if (!event.isCancelled()) {
                        recipient.sendMessage(censoredFormat);
                    }
                } else {
                    if (!event.isCancelled()) {
                        recipient.sendMessage(format);
                    }
                }
            }

            this.plugin.getServer().getConsoleSender().sendMessage(format);

            event.setCancelled(true);
        }
    }
}
