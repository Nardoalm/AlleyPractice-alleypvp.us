package us.alleypvp.practice.feature.hologram.command;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.feature.hologram.leaderboard.KitLeaderboardHologram;
import us.alleypvp.practice.feature.kit.Kit;
import us.alleypvp.practice.feature.kit.KitService;
import us.alleypvp.practice.feature.leaderboard.LeaderboardType;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

public class HologramCommand extends BaseCommand {

    @CommandData(
            name = "leaderboardnpc",
            aliases = {"lbnpc", "hologram", "lbh"},
            isAdminOnly = true,
            usage = "leaderboardnpc <spawn|remove|update> <kit/global> <type>",
            description = "Manage practice leaderboard holograms."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args == null || args.length == 0) {
            command.sendUsage();
            player.sendMessage(CC.translate("&7Types available: RANKED, UNRANKED, WIN_STREAK, FFA"));
            return;
        }

        String action = args[0].toLowerCase();
        us.alleypvp.practice.feature.hologram.manager.HologramManager hologramManager = AlleyPractice.getInstance().getService(us.alleypvp.practice.feature.hologram.manager.HologramManager.class);

        if (action.equals("update")) {
            for (KitLeaderboardHologram holo : hologramManager.getActiveHolograms()) {
                holo.update();
            }
            player.sendMessage(CC.translate("&aForce updated all active leaderboard holograms."));
            return;
        }

        if (args.length < 3) {
            command.sendUsage();
            player.sendMessage(CC.translate("&7Types available: RANKED, UNRANKED, WIN_STREAK, FFA"));
            return;
        }

        String targetType = args[1];
        boolean isGlobal = targetType.equalsIgnoreCase("global");

        if (!isGlobal) {
            Kit kit = AlleyPractice.getInstance().getService(KitService.class).getKit(targetType);
            if (kit == null) {
                player.sendMessage(CC.translate("&cKit or 'global' identifier not found."));
                return;
            }
        }

        LeaderboardType type;
        try {
            type = LeaderboardType.valueOf(args[2].toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(CC.translate("&cInvalid type."));
            return;
        }

        switch (action) {
            case "spawn":
                hologramManager.saveHologram(player.getLocation(), isGlobal ? "global" : targetType, type);
                player.sendMessage(CC.translate("&aSpawned &b" + targetType.toUpperCase() + " " + type.name() + " &aland saved to storage."));
                break;

            case "remove":
                boolean removed = hologramManager.removeHologram(targetType, type);
                if (removed) {
                    player.sendMessage(CC.translate("&cRemoved &b" + targetType.toUpperCase() + " " + type.name() + " &cfrom world and storage."));
                } else {
                    player.sendMessage(CC.translate("&cLeaderboard hologram not found in configurations."));
                }
                break;

            default:
                command.sendUsage();
                break;
        }
    }
}