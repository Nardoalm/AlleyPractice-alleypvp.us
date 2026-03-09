package com.kaosmc.practice.feature.arena.command.impl.manage;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import com.kaosmc.practice.feature.arena.Arena;
import com.kaosmc.practice.feature.arena.ArenaService;
import com.kaosmc.practice.feature.arena.internal.types.FreeForAllArena;
import com.kaosmc.practice.feature.arena.internal.types.StandAloneArena;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.Locale;
import java.util.Objects;

/**
 * @author Emmy
 * @project Kaos
 * @date 24/09/2024 - 18:29
 */
public class ArenaViewCommand extends BaseCommand {
    @CommandData(
            name = "arena.view",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "arena view <arenaName>",
            description = "Mostra informações detalhadas sobre uma arena"
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        ArenaService arenaService = this.plugin.getService(ArenaService.class);

        Arena arena = arenaService.getArenaByName(args[0]);
        if (arena == null) {
            sender.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_NOT_FOUND).replace("{arena-name}", args[0]));
            return;
        }

        //TODO: add remaining arena details

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&6&lArena " + arena.getName() + " &f(" + (arena.isEnabled() ? "&aAtivada" : "&cDesativada") + "&f)"));
        sender.sendMessage(CC.translate(" &6&l│ &rNome de Exibição: &6" + arena.getDisplayName()));
        sender.sendMessage(CC.translate(" &6&l│ &rTipo: &6" + arena.getType()));
        sender.sendMessage(CC.translate(" &6&l│ &rKits: &6" + (arena.getKits().isEmpty() ? "Nenhum" : String.join(", ", arena.getKits()))));
        if (arena instanceof FreeForAllArena) {
            FreeForAllArena ffaArena = (FreeForAllArena) arena;
            sender.sendMessage(CC.translate(" &6&l│ &rZonas Seguras:"));
            sender.sendMessage(CC.translate("   &6&l◆ &fPos1 &7(Mínimo)&f: &6" + (ffaArena.getMinimum() != null ? this.formatBlockLocation(ffaArena.getMinimum()) : "Não Definido")));
            sender.sendMessage(CC.translate("   &6&l◆ &fPos2 &7(Máximo)&f: &6" + (ffaArena.getMaximum() != null ? this.formatBlockLocation(ffaArena.getMaximum()) : "Não Definido")));
            sender.sendMessage(CC.translate(" &6&l│ &rPosições:"));
            sender.sendMessage(CC.translate("   &6&l◆ &fCentro &7(Espectador)&f: &6" + (ffaArena.getCenter() != null ? formatFullLocation(ffaArena.getCenter()) : "&cNulo")));
            sender.sendMessage(CC.translate("   &6&l◆ &fPos1: &6" + (ffaArena.getPos1() != null ? this.formatFullLocation(ffaArena.getPos1()) : "&cNulo")));
        } else {
            sender.sendMessage(CC.translate(" &6&l│ &rMínimo: &6" + (arena.getMinimum() != null ? this.formatBlockLocation(arena.getMinimum()) : "Não Definido")));
            sender.sendMessage(CC.translate(" &6&l│ &rMáximo: &6" + (arena.getMaximum() != null ? this.formatBlockLocation(arena.getMaximum()) : "Não Definido")));
            sender.sendMessage(CC.translate(" &6&l│ &rPosições:"));
            sender.sendMessage(CC.translate("   &6&l◆ &fCentro &7(Espectador)&f: &6" + (arena.getCenter() != null ? this.formatFullLocation(arena.getCenter()) : "&cNulo")));
            sender.sendMessage(CC.translate("   &6&l◆ &fAzul: &6" + (arena.getPos1() != null ? this.formatFullLocation(arena.getPos1()) : "&cNulo")));
            sender.sendMessage(CC.translate("   &6&l◆ &fVermelho: &6" + (arena.getPos2() != null ? this.formatFullLocation(arena.getPos2()) : "&cNulo")));
        }

        if (arena instanceof StandAloneArena) {
            StandAloneArena standaloneArena = (StandAloneArena) arena;
            sender.sendMessage(CC.translate(" &6&l│ &rPortais dos Times:"));
            sender.sendMessage(CC.translate("   &6&l◆ &fAzul: &6" + (standaloneArena.getTeam1Portal() != null ? this.formatBlockLocation(standaloneArena.getTeam1Portal()) : "&cNulo")));
            sender.sendMessage(CC.translate("   &6&l◆ &fVermelho: &6" + (standaloneArena.getTeam2Portal() != null ? this.formatBlockLocation(standaloneArena.getTeam2Portal()) : "&cNulo")));
            sender.sendMessage(CC.translate(" &6&l│ &rRaio do Portal: &6" + standaloneArena.getPortalRadius()));
            sender.sendMessage(CC.translate(" &6&l│ &rLimite de Altura: &6" + standaloneArena.getHeightLimit()));
            sender.sendMessage(CC.translate(" &6&l│ &rNível do Void: &6" + standaloneArena.getVoidLevel()));
        }

        sender.sendMessage("");
    }

    private String formatBlockLocation(Location loc) {
        if (loc == null) return "Não Definido";

        return String.format(Locale.ENGLISH, "%.1f, %.1f, %.1f &7[%s]",
                loc.getX(),
                loc.getY(),
                loc.getZ(),
                Objects.requireNonNull(loc.getWorld()).getName()
        );
    }

    private String formatFullLocation(Location loc) {
        if (loc == null) return "&cNulo";

        return String.format(Locale.ENGLISH, "%.1f, %.1f, %.1f, &7%.0f, %.0f &7[%s]",
                loc.getX(),
                loc.getY(),
                loc.getZ(),
                loc.getPitch(),
                loc.getYaw(),
                Objects.requireNonNull(loc.getWorld()).getName()
        );
    }
}
