package com.kaosmc.practice.feature.command.impl.main.impl;

import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import com.kaosmc.practice.feature.arena.ArenaService;
import com.kaosmc.practice.feature.combat.CombatService;
import com.kaosmc.practice.feature.cooldown.CooldownService;
import com.kaosmc.practice.feature.kit.KitService;
import com.kaosmc.practice.feature.kit.setting.KitSettingService;
import com.kaosmc.practice.feature.queue.QueueService;
import com.kaosmc.practice.feature.emoji.EmojiService;
import com.kaosmc.practice.feature.duel.DuelRequestService;
import com.kaosmc.practice.feature.match.MatchService;
import com.kaosmc.practice.feature.match.snapshot.SnapshotService;
import com.kaosmc.practice.feature.party.PartyService;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.common.text.CC;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

/**
 * @author Emmy
 * @project Kaos
 * @date 30/05/2024 - 12:15
 */
public class KaosDebugCommand extends BaseCommand {

    @CommandData(
            name = "kaos.debug",
            isAdminOnly = true,
            usage = "kaos debug <memory/instance/profile/profileData>",
            description = "Exibe informacoes de depuracao."
    )

    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();
        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        switch (args[0]) {
            case "memory":
                this.sendMemoryInfo(player);
                break;
            case "instance":
                this.sendInstanceInfo(player);
                break;
            case "profile":
                this.sendProfileInfo(profile, player);
                break;
            case "profiledata":
                this.sendProfileData(profile, player);
                break;
            default:
                command.sendUsage();
                break;
        }
    }

    /**
     * Sends memory information to the player.
     *
     * @param player The player to send the information to.
     */
    private void sendMemoryInfo(Player player) {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = allocatedMemory - freeMemory;

        Arrays.asList(
                "",
                "     &6&lKaos &7│ &fInformacoes de Memoria",
                "      &6&l│ &fMemoria Maxima: &6" + this.formatNumber((int) (maxMemory / 1024 / 1024)) + "MB",
                "      &6&l│ &fMemoria Alocada: &6" + this.formatNumber((int) (allocatedMemory / 1024 / 1024)) + "MB",
                "      &6&l│ &fMemoria Livre: &6" + this.formatNumber((int) (freeMemory / 1024 / 1024)) + "MB",
                "      &6&l│ &fMemoria Usada: &6" + this.formatNumber((int) (usedMemory / 1024 / 1024)) + "MB",
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));
    }

    /**
     * Sends instance information to the player.
     *
     * @param player The player to send the information to.
     */
    private void sendInstanceInfo(Player player) {
        Arrays.asList(
                "",
                "     &6&lKaos &7│ &fInformacoes da Instancia",
                "      &6&l│ &fPerfis: &6" + this.formatNumber(this.plugin.getService(ProfileService.class).getProfiles().size()),
                "      &6&l│ &fPartidas: &6" + this.formatNumber(this.plugin.getService(MatchService.class).getMatches().size()),
                "      &6&l│ &fFilas: &6" + this.formatNumber(this.plugin.getService(QueueService.class).getQueues().size()),
                "      &6&l│ &fPerfis em fila: &6" + this.formatNumber(Arrays.stream(this.plugin.getService(QueueService.class).getQueues().stream().mapToInt(queue -> queue.getProfiles().size()).toArray()).sum()),
                "      &6&l│ &fCooldowns: &6" + this.formatNumber(this.plugin.getService(CooldownService.class).getCooldowns().size()),
                "      &6&l│ &fCooldowns ativos: &6" + this.formatNumber((int) this.plugin.getService(CooldownService.class).getCooldowns().stream().filter(cooldown -> cooldown.getC().isActive()).count()),
                "      &6&l│ &fCombates: &6" + this.formatNumber(this.plugin.getService(CombatService.class).getCombatMap().size()),
                "      &6&l│ &fKits: &6" + this.formatNumber(this.plugin.getService(KitService.class).getKits().size()),
                "      &6&l│ &fConfiguracoes de kit: &6" + this.formatNumber(this.plugin.getService(KitSettingService.class).getSettings().size()),
                "      &6&l│ &fParties: &6" + this.formatNumber(this.plugin.getService(PartyService.class).getParties().size()),
                "      &6&l│ &fArenas: &6" + this.formatNumber(this.plugin.getService(ArenaService.class).getArenas().size()),
                "      &6&l│ &fSnapshots: &6" + this.formatNumber(this.plugin.getService(SnapshotService.class).getSnapshots().size()),
                "      &6&l│ &fPedidos de duelo: &6" + this.formatNumber(this.plugin.getService(DuelRequestService.class).getDuelRequests().size()),
                "      &6&l│ &fEmojis: &6" + this.formatNumber(this.plugin.getService(EmojiService.class).getEmojis().size()),
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));
    }

    /**
     * Sends profile information to the player.
     *
     * @param profile The profile to send the information for.
     * @param player  The player to send the information to.
     */
    private void sendProfileInfo(Profile profile, Player player) {
        String banned = profile.getProfileData().isRankedBanned() ? "&c&lBANIDO" : "&a&lNAO BANIDO";
        Arrays.asList(
                "",
                "     &6&lProfile &7│ &f" + profile.getName(),
                "      &6&l│ &fUUID: &6" + profile.getUuid(),
                "      &6&l│ &fElo: &6" + this.formatNumber(profile.getProfileData().getElo()),
                "      &6&l│ &fCoins: &6" + this.formatNumber(profile.getProfileData().getCoins()),
                "      &6&l│ &fEstado: &6" + profile.getState() + " &7(" + profile.getState().getDescription() + ")",
                "      &6&l│ &fFila atual: &6" + (profile.getQueueProfile() != null ? profile.getQueueProfile().getQueue().getKit().getName() : "&c&lNULO"),
                "      &6&l│ &fRanked: &6" + banned,
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line).replace("The player", profile.getName())));
    }

    /**
     * Sends profile data to the player.
     *
     * @param profile The profile to send the data for.
     * @param player  The player to send the data to.
     */
    private void sendProfileData(Profile profile, Player player) {
        Arrays.asList(
                "",
                "     &6&lDados do Perfil &7│ &f" + profile.getName(),
                "      &6&l│ &fVitorias unranked: &6" + this.formatNumber(profile.getProfileData().getUnrankedWins()),
                "      &6&l│ &fDerrotas unranked: &6" + this.formatNumber(profile.getProfileData().getUnrankedLosses()),
                "      &6&l│ &fVitorias ranked: &6" + this.formatNumber(profile.getProfileData().getRankedWins()),
                "      &6&l│ &fDerrotas ranked: &6" + this.formatNumber(profile.getProfileData().getRankedLosses()),
                "      &6&l│ &fAbates FFA: &6" + this.formatNumber(profile.getProfileData().getTotalFFAKills()),
                "      &6&l│ &fMortes FFA: &6" + this.formatNumber(profile.getProfileData().getTotalFFADeaths()),
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));
    }

    /**
     * Formats a number with commas.
     *
     * @param number The number to format
     * @return The formatted number
     */
    private String formatNumber(int number) {
        return NumberFormat.getInstance(Locale.US).format(number);
    }
}
