package us.alleypvp.practice.feature.emoji.listener;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.core.locale.LocaleService;
import us.alleypvp.practice.core.locale.internal.impl.SettingsLocaleImpl;
import us.alleypvp.practice.feature.emoji.EmojiService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 10/11/2024 - 09:34
 */
public class EmojiListener implements Listener {
    @EventHandler
    private void onAsyncChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (!player.hasPermission(AlleyPractice.getInstance().getService(LocaleService.class).getString(SettingsLocaleImpl.PERMISSION_DONATOR_EMOJI_USAGE))) {
            return;
        }

        for (Map.Entry<String, String> entry : AlleyPractice.getInstance().getService(EmojiService.class).getEmojis().entrySet()) {
            if (message.contains(entry.getKey())) {
                message = message.replace(entry.getKey(), entry.getValue());
            }
        }

        event.setMessage(message);
    }
}