package com.kaosmc.practice.feature.arena.command.impl.manage;

import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import com.kaosmc.practice.feature.arena.ArenaService;
import com.kaosmc.practice.feature.arena.internal.types.StandAloneArena;
import com.kaosmc.practice.feature.match.Match;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * @author Remi
 * @project kaos-practice
 * @date 20/06/2025
 */
public class ArenaTestCommand extends BaseCommand {
    @CommandData(
            name = "arena.test",
            isAdminOnly = true,
            usage = "arena test",
            description = "Testa várias propriedades da arena para depuração"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("World: " + player.getWorld());
        player.sendMessage("Location: " + player.getLocation());

        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        player.sendMessage("Copied arenas: " + arenaService.getTemporaryArenas().size());

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        Match match = profile.getMatch();
        if (match != null) {
            player.sendMessage("Match Arena: " + match.getArena());

            StandAloneArena arena = (StandAloneArena) match.getArena();
            if (arena != null) {
                player.sendMessage("Arena Name: " + arena.getName());
                player.sendMessage("Arena Type: " + arena.getType());
                player.sendMessage("Arena Positions: " + arena.getPos1() + " - " + arena.getPos2());
                player.sendMessage("Arena Display Name: " + arena.getDisplayName());
                player.sendMessage("Arena World " + Objects.requireNonNull(arena.getMinimum().getWorld()).getName());
                player.sendMessage("Is copied: " + arena.isTemporaryCopy());
                player.sendMessage("Arena Center: " + arena.getCenter());
                player.sendMessage("Arena Enabled: " + arena.isEnabled());
                arena.verifyArenaExists();
            } else {
                player.sendMessage("No arena found for this match.");
            }
        } else {
            player.sendMessage("No match found for this profile.");
        }
    }
}
