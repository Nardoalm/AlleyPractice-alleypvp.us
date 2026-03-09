package com.kaosmc.practice.core.profile.command.admin;

import com.kaosmc.practice.common.PlayerUtil;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.common.time.DateFormat;
import com.kaosmc.practice.common.time.DateFormatter;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.data.types.ProfilePlayTimeData;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Kaos
 * @date 24/05/2024 - 18:45
 */
public class PlaytimeCommand extends BaseCommand {
    @CommandData(
            name = "playtime",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "playtime <player>",
            description = "Verifica o tempo de jogo de um jogador."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            command.sendUsage();
            return;
        }

        OfflinePlayer targetPlayer = PlayerUtil.getOfflinePlayerByName(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        Profile targetProfile = this.plugin.getService(ProfileService.class).getProfile(targetPlayer.getUniqueId());
        if (targetProfile == null) {
            sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        ProfilePlayTimeData playTimeData = targetProfile.getProfileData().getPlayTimeData();

        long totalPlayTime = playTimeData.getTotal();
        long totalTimeInSeconds = totalPlayTime / 1000;
        int days = (int) (totalTimeInSeconds / 86400);
        int hours = (int) (totalTimeInSeconds / 3600);
        int minutes = (int) ((totalTimeInSeconds % 3600) / 60);
        int seconds = (int) (totalTimeInSeconds % 60);

        long firstJoin = targetProfile.getFirstJoin();
        DateFormatter firstJoinFormatted = new DateFormatter(DateFormat.TIME_PLUS_DATE, firstJoin);

        List<String> messages = new ArrayList<>();
        messages.add("");
        messages.add("&6&lTempo de Jogo de " + targetPlayer.getName());
        messages.add("  &f&l● &6Dias: &f" + days);
        messages.add("  &f&l● &6Horas: &f" + hours);
        messages.add("  &f&l● &6Minutos: &f" + minutes);
        messages.add("  &f&l● &6Segundos: &f" + seconds);
        messages.add("");
        messages.add("&fA primeira entrada foi em &6" + firstJoinFormatted.setFancy(ChatColor.AQUA, ChatColor.WHITE) + "&f.");
        messages.add("");

        if (targetProfile.isOnline()) {
            messages.add(" &c&lAviso: &7" + targetPlayer.getName() + " está online agora.");
            messages.add(" &7O tempo de jogo será atualizado quando ele sair.");
            messages.add("");
        }

        messages.forEach(message -> sender.sendMessage(CC.translate(message)));
    }
}
