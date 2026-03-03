package com.kaosmc.practice.feature.command;

import com.kaosmc.practice.core.profile.command.player.*;
import com.kaosmc.practice.core.profile.command.player.setting.toggle.*;
import com.kaosmc.practice.core.profile.command.player.setting.worldtime.*;
import com.kaosmc.practice.feature.arena.command.ArenaCommand;
import com.kaosmc.practice.feature.command.impl.other.InvSeeCommand;
import com.kaosmc.practice.feature.command.impl.other.MoreCommand;
import com.kaosmc.practice.feature.command.impl.other.RefillCommand;
import com.kaosmc.practice.feature.command.impl.other.SudoAllCommand;
import com.kaosmc.practice.feature.command.impl.other.troll.*;
import com.kaosmc.practice.feature.cooldown.command.CooldownResetCommand;
import com.kaosmc.practice.feature.kit.command.KitCommand;
import com.kaosmc.practice.feature.kit.command.helper.impl.EnchantCommand;
import com.kaosmc.practice.feature.kit.command.helper.impl.PotionDurationCommand;
import com.kaosmc.practice.feature.kit.command.helper.impl.RemoveEnchantsCommand;
import com.kaosmc.practice.feature.kit.command.helper.impl.RenameCommand;
import com.kaosmc.practice.feature.match.command.player.*;
import com.kaosmc.practice.feature.queue.command.admin.QueueCommand;
import com.kaosmc.practice.feature.queue.command.player.LeaveQueueCommand;
import com.kaosmc.practice.feature.queue.command.player.QueuesCommand;
import com.kaosmc.practice.feature.queue.command.player.UnrankedCommand;
import com.kaosmc.practice.feature.spawn.command.SetSpawnCommand;
import com.kaosmc.practice.feature.spawn.command.SpawnCommand;
import com.kaosmc.practice.feature.spawn.command.SpawnItemsCommand;
import com.kaosmc.practice.feature.command.impl.main.KaosCommand;
import dev.revere.kaos.feature.command.impl.other.*;
import com.kaosmc.practice.feature.command.impl.other.gamemode.AdventureCommand;
import com.kaosmc.practice.feature.command.impl.other.gamemode.CreativeCommand;
import com.kaosmc.practice.feature.command.impl.other.gamemode.SpectatorCommand;
import com.kaosmc.practice.feature.command.impl.other.gamemode.SurvivalCommand;
import dev.revere.kaos.feature.command.impl.other.troll.*;
import com.kaosmc.practice.feature.abilities.command.AbilityCommand;
import com.kaosmc.practice.feature.cosmetic.command.CosmeticCommand;
import com.kaosmc.practice.feature.division.command.DivisionCommand;
import com.kaosmc.practice.feature.emoji.command.EmojiCommand;
import com.kaosmc.practice.feature.layout.command.LayoutCommand;
import com.kaosmc.practice.feature.leaderboard.command.LeaderboardCommand;
import com.kaosmc.practice.feature.level.command.LevelAdminCommand;
import com.kaosmc.practice.feature.server.command.ServiceCommand;
import com.kaosmc.practice.feature.tip.command.TipCommand;
import com.kaosmc.practice.feature.title.command.TitleCommand;
import com.kaosmc.practice.feature.duel.command.AcceptCommand;
import com.kaosmc.practice.feature.duel.command.DuelCommand;
import com.kaosmc.practice.feature.duel.command.DuelRequestsCommand;
import com.kaosmc.practice.feature.ffa.command.FFACommand;
import com.kaosmc.practice.feature.host.command.HostCommand;
import com.kaosmc.practice.feature.match.command.admin.MatchCommand;
import dev.revere.kaos.feature.match.command.player.*;
import com.kaosmc.practice.feature.match.snapshot.command.InventoryCommand;
import com.kaosmc.practice.feature.party.command.PartyCommand;
import com.kaosmc.practice.core.profile.command.admin.PlaytimeCommand;
import com.kaosmc.practice.core.profile.data.command.ranked.RankedCommand;
import com.kaosmc.practice.core.profile.data.command.ResetStatsCommand;
import com.kaosmc.practice.core.profile.data.command.coin.SetCoinsCommand;
import dev.revere.kaos.core.profile.command.player.*;
import com.kaosmc.practice.core.profile.command.player.setting.MatchSettingsCommand;
import com.kaosmc.practice.core.profile.command.player.setting.PracticeSettingsCommand;
import dev.revere.kaos.core.profile.command.player.setting.toggle.*;
import dev.revere.kaos.core.profile.command.player.setting.worldtime.*;
import com.kaosmc.practice.common.logger.Logger;
import com.kaosmc.practice.common.logger.command.ViewErrorCommand;
import lombok.experimental.UtilityClass;

/**
 * @author Emmy
 * @project Kaos
 * @date 31/12/2024 - 23:24
 */
@UtilityClass
public class CommandUtility {
    /**
     * Registers all commands.
     */
    public void registerCommands() {
        Logger.logTimeWithAction("registered", "Commands", () -> {
            new KaosCommand();

            new AdventureCommand();
            new CreativeCommand();
            new SurvivalCommand();
            new SpectatorCommand();

            new KitCommand();
            new ArenaCommand();
            new MatchCommand();
            new RankedCommand();
            new QueueCommand();
            new FFACommand();
            new CosmeticCommand();
            new DivisionCommand();
            new TitleCommand();
            new LevelAdminCommand();
            new LevelCommand();
            new ServiceCommand();
            new EnchantCommand();
            new SudoAllCommand();
            new InvSeeCommand();
            new MoreCommand();
            new PotionDurationCommand();
            new RefillCommand();
            new RemoveEnchantsCommand();
            new RenameCommand();
            new PlaytimeCommand();
            new SpawnItemsCommand();
            new SetSpawnCommand();
            new SpawnCommand();
            new SetCoinsCommand();

            new FakeExplosionCommand();
            new HeartAttackCommand();
            new LaunchCommand();
            new PushCommand();
            new StrikeCommand();
            new TrollCommand();

            new CooldownResetCommand();

            new ViewErrorCommand();

            new AbilityCommand();

            new ChatCommand();
            new DayCommand();
            new NightCommand();
            new SunsetCommand();
            new ResetTimeCommand();
            new TogglePartyInvitesCommand();
            new TogglePartyMessagesCommand();
            new ToggleScoreboardCommand();
            new ToggleScoreboardLinesCommand();
            new ToggleTablistCommand();
            new ToggleWorldTimeCommand();
            new ToggleProfanityFilterCommand();

            new PartyCommand();
            new AcceptCommand();
            new DuelCommand();
            new DuelRequestsCommand();
            new InventoryCommand();
            new PracticeSettingsCommand();
            new LeaderboardCommand();
            new ResetStatsCommand();
            new StatsCommand();
            new SpectateCommand();
            new ViewPlayersCommand();
            new LeaveSpectatorCommand();
            new LeaveMatchCommand();
            new CurrentMatchesCommand();
            new LeaveQueueCommand();
            new QueuesCommand();
            new UnrankedCommand();
            new MatchSettingsCommand();

            new ShopCommand();
            new ChallengesCommand();
            new MatchHistoryCommand();
            new TipCommand();
            new LayoutCommand();

            new HostCommand();
            new EmojiCommand();
        });
    }
}