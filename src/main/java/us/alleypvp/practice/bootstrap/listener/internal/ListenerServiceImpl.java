package us.alleypvp.practice.bootstrap.listener.internal;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.feature.cosmetic.CosmeticListener;
import us.alleypvp.practice.feature.match.listener.types.*;
import us.alleypvp.practice.library.menu.MenuListener;
import us.alleypvp.practice.feature.arena.listener.ArenaListener;
import us.alleypvp.practice.feature.combat.listener.CombatListener;
import us.alleypvp.practice.feature.event.listener.EventListener;
import us.alleypvp.practice.feature.hotbar.listener.HotbarListener;
import us.alleypvp.practice.feature.queue.listener.QueueListener;
import us.alleypvp.practice.feature.server.listener.CraftingListener;
import us.alleypvp.practice.feature.spawn.listener.SpawnListener;
import us.alleypvp.practice.bootstrap.listener.ListenerService;
import us.alleypvp.practice.feature.emoji.listener.EmojiListener;
import us.alleypvp.practice.feature.item.listener.ItemListener;
import us.alleypvp.practice.feature.layout.listener.LayoutListener;
import us.alleypvp.practice.feature.ffa.listener.FFAListener;
import us.alleypvp.practice.feature.ffa.listener.FFABlockListener;
import us.alleypvp.practice.feature.ffa.listener.FFACuboidListener;
import us.alleypvp.practice.feature.ffa.listener.FFADamageListener;
import us.alleypvp.practice.feature.ffa.listener.FFADisconnectListener;
import us.alleypvp.practice.feature.match.listener.MatchListener;
import us.alleypvp.practice.feature.match.snapshot.listener.SnapshotListener;
import us.alleypvp.practice.feature.party.listener.PartyListener;
import us.alleypvp.practice.bootstrap.KaosContext;
import us.alleypvp.practice.bootstrap.annotation.Service;
import us.alleypvp.practice.core.profile.listener.ProfileListener;

import java.util.Arrays;

/**
 * @author Emmy
 * @project kaos-practice
 * @since 16/07/2025
 */
@Service(provides = ListenerService.class, priority = 1000)
public class ListenerServiceImpl implements ListenerService {

    @Override
    public void initialize(KaosContext context) {
        this.registerListeners(context.getPlugin());
    }

    @Override
    public void registerListeners(AlleyPractice plugin) {
        Arrays.asList(
                new ProfileListener(),
                new HotbarListener(),
                new EventListener(),
                new PartyListener(),
                new ArenaListener(),
                new MenuListener(),
                new SpawnListener(),

                new EmojiListener(),
                new CombatListener(),
                new QueueListener(),
                new LayoutListener(),
                new SnapshotListener(),
                new CraftingListener(),

                new ItemListener(),

                new FFAListener(), new FFACuboidListener(),
                new FFABlockListener(), new FFADamageListener(), new FFADisconnectListener(),

                new MatchListener(), new MatchInteractListener(),
                new MatchPearlListener(), new MatchDisconnectListener(),
                new MatchDamageListener(), new MatchChatListener(), new MatchBlockListener(),

                new CosmeticListener()

        ).forEach(listener -> plugin.getServer().getPluginManager().registerEvents(listener, plugin));
    }
}
