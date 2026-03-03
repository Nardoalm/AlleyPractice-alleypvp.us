package com.kaosmc.practice.bootstrap.listener.internal;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.adapter.core.listener.CoreChatListener;
import com.kaosmc.practice.feature.cosmetic.CosmeticListener;
import com.kaosmc.practice.feature.match.listener.types.*;
import com.kaosmc.practice.library.menu.MenuListener;
import com.kaosmc.practice.feature.arena.listener.ArenaListener;
import com.kaosmc.practice.feature.combat.listener.CombatListener;
import com.kaosmc.practice.feature.hotbar.listener.HotbarListener;
import com.kaosmc.practice.feature.queue.listener.QueueListener;
import com.kaosmc.practice.feature.server.listener.CraftingListener;
import com.kaosmc.practice.feature.spawn.listener.SpawnListener;
import com.kaosmc.practice.bootstrap.listener.ListenerService;
import com.kaosmc.practice.feature.emoji.listener.EmojiListener;
import com.kaosmc.practice.feature.item.listener.ItemListener;
import com.kaosmc.practice.feature.layout.listener.LayoutListener;
import com.kaosmc.practice.feature.ffa.listener.FFAListener;
import com.kaosmc.practice.feature.ffa.listener.FFABlockListener;
import com.kaosmc.practice.feature.ffa.listener.FFACuboidListener;
import com.kaosmc.practice.feature.ffa.listener.FFADamageListener;
import com.kaosmc.practice.feature.ffa.listener.FFADisconnectListener;
import com.kaosmc.practice.feature.match.listener.MatchListener;
import dev.revere.kaos.feature.match.listener.types.*;
import com.kaosmc.practice.feature.match.snapshot.listener.SnapshotListener;
import com.kaosmc.practice.feature.party.listener.PartyListener;
import com.kaosmc.practice.bootstrap.KaosContext;
import com.kaosmc.practice.bootstrap.annotation.Service;
import com.kaosmc.practice.core.profile.listener.ProfileListener;

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
    public void registerListeners(KaosPractice plugin) {
        Arrays.asList(
                new ProfileListener(),
                new HotbarListener(),
                new PartyListener(),
                new ArenaListener(),
                new MenuListener(),
                new SpawnListener(),

                new EmojiListener(),
                new CombatListener(),
                new QueueListener(),
                new CoreChatListener(),
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