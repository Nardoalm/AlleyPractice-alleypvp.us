package com.kaosmc.practice.feature.leaderboard.internal;

import com.mongodb.client.MongoCollection;
import com.kaosmc.practice.feature.kit.KitService;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.bootstrap.KaosContext;
import com.kaosmc.practice.bootstrap.annotation.Service;
import com.kaosmc.practice.core.database.MongoService;
import com.kaosmc.practice.feature.leaderboard.LeaderboardService;
import com.kaosmc.practice.feature.leaderboard.data.LeaderboardPlayerData;
import com.kaosmc.practice.feature.leaderboard.LeaderboardType;
import com.kaosmc.practice.feature.leaderboard.model.LeaderboardRecord;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.data.ProfileData;
import com.kaosmc.practice.core.profile.data.types.ProfileFFAData;
import com.kaosmc.practice.core.profile.data.types.ProfileRankedKitData;
import com.kaosmc.practice.core.profile.data.types.ProfileUnrankedKitData;
import com.kaosmc.practice.common.logger.Logger;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Kaos
 * @since 03/03/2025
 */
@Getter
@Service(provides = LeaderboardService.class, priority = 280)
public class LeaderboardServiceImpl implements LeaderboardService {
    private final MongoService mongoService;
    private final KitService kitService;
    private final ProfileService profileService;
    private final ExecutorService executorService;

    private final Map<Kit, List<LeaderboardRecord>> leaderboardCache = new ConcurrentHashMap<>();
    private final Map<String, Integer> onlinePlayerCache = new ConcurrentHashMap<>();

    /**
     * Constructor for DI.
     */
    public LeaderboardServiceImpl(MongoService mongoService, KitService kitService, ProfileService profileService) {
        this.mongoService = mongoService;
        this.kitService = kitService;
        this.profileService = profileService;
        this.executorService = Executors.newFixedThreadPool(4);
    }

    @Override
    public void initialize(KaosContext context) {
        this.forceRecalculateAll();
    }

    @Override
    public void shutdown(KaosContext context) {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            Logger.info("O serviço executor da leaderboard foi encerrado.");
        }
    }

    @Override
    public void forceRecalculateAll() {
        MongoCollection<Document> profileCollection = this.mongoService.getMongoDatabase().getCollection("profiles");

        CompletableFuture.allOf(this.kitService.getKits().stream()
                .map(kit -> CompletableFuture.runAsync(() -> calculateLeaderboardForKit(kit, profileCollection), executorService)).toArray(CompletableFuture[]::new)).join();
    }

    private void calculateLeaderboardForKit(Kit kit, MongoCollection<Document> profileCollection) {
        List<LeaderboardRecord> records = Collections.synchronizedList(new ArrayList<>());

        for (LeaderboardType type : LeaderboardType.values()) {
            List<LeaderboardPlayerData> playerDataList = fetchOptimizedLeaderboard(profileCollection, kit, type);
            records.add(new LeaderboardRecord(type, playerDataList));
        }

        this.leaderboardCache.put(kit, records);
    }

    private List<LeaderboardPlayerData> fetchOptimizedLeaderboard(MongoCollection<Document> profileCollection, Kit kit, LeaderboardType type) {
        List<LeaderboardPlayerData> playerDataList = new ArrayList<>();

        List<Document> pipeline = buildAggregationPipeline(kit, type);

        for (Document doc : profileCollection.aggregate(pipeline)) {
            try {
                String name = doc.getString("name");
                UUID uuid = UUID.fromString(doc.getString("uuid"));
                int value = doc.getInteger("value", 0);

                if (value > 0 || type == LeaderboardType.RANKED) {
                    playerDataList.add(new LeaderboardPlayerData(name, uuid, kit, value));
                }
            } catch (Exception ignored) {
            }
        }

        return playerDataList;
    }

    private List<Document> buildAggregationPipeline(Kit kit, LeaderboardType type) {
        List<Document> pipeline = new ArrayList<>();

        Document projectStage = new Document("$project", new Document()
                .append("uuid", 1)
                .append("name", 1)
                .append("value", buildValueExtraction(kit, type)));

        pipeline.add(projectStage);

        if (type != LeaderboardType.RANKED) {
            pipeline.add(new Document("$match", new Document("value", new Document("$gt", 0))));
        }

        pipeline.add(new Document("$sort", new Document("value", -1)));

        pipeline.add(new Document("$limit", 100));

        return pipeline;
    }

    private Document buildValueExtraction(Kit kit, LeaderboardType type) {
        String kitName = kit.getName();

        switch (type) {
            case RANKED:
                return new Document("$ifNull", Arrays.asList(
                        new Document("$getField", new Document()
                                .append("field", "elo")
                                .append("input", new Document("$getField", new Document()
                                        .append("field", kitName)
                                        .append("input", "$profileData.rankedKitData")))),
                        1000
                ));
            case UNRANKED:
                return new Document("$ifNull", Arrays.asList(
                        new Document("$getField", new Document()
                                .append("field", "wins")
                                .append("input", new Document("$getField", new Document()
                                        .append("field", kitName)
                                        .append("input", "$profileData.unrankedKitData")))),
                        0
                ));
            case FFA:
                return new Document("$ifNull", Arrays.asList(
                        new Document("$getField", new Document()
                                .append("field", "kills")
                                .append("input", new Document("$getField", new Document()
                                        .append("field", kitName)
                                        .append("input", "$profileData.ffaData")))),
                        0
                ));
            case WIN_STREAK:
                return new Document("$ifNull", Arrays.asList(
                        new Document("$getField", new Document()
                                .append("field", "winstreak")
                                .append("input", new Document("$getField", new Document()
                                        .append("field", kitName)
                                        .append("input", "$profileData.unrankedKitData")))),
                        0
                ));
            default:
                return new Document("$literal", 0);
        }
    }

    @Override
    public List<LeaderboardPlayerData> getLeaderboardEntries(Kit kit, LeaderboardType type) {
        this.refreshOnlinePlayersOptimized(kit, type);

        return this.leaderboardCache.getOrDefault(kit, Collections.emptyList())
                .stream()
                .filter(record -> record.getType() == type)
                .findFirst()
                .map(LeaderboardRecord::getParticipants)
                .orElse(Collections.emptyList());
    }

    private void refreshOnlinePlayersOptimized(Kit kit, LeaderboardType type) {
        LeaderboardRecord record = this.leaderboardCache.getOrDefault(kit, Collections.emptyList())
                .stream()
                .filter(r -> r.getType() == type)
                .findFirst()
                .orElse(null);

        if (record == null) return;

        List<LeaderboardPlayerData> leaderboard = record.getParticipants();

        Map<UUID, Integer> onlinePlayerUpdates = new HashMap<>();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Profile profile = this.profileService.getProfile(onlinePlayer.getUniqueId());
            if (profile != null) {
                int newValue = getValueForType(profile, kit, type);
                onlinePlayerUpdates.put(profile.getUuid(), newValue);
            }
        }

        for (LeaderboardPlayerData playerData : leaderboard) {
            Integer newValue = onlinePlayerUpdates.get(playerData.getUuid());
            if (newValue != null) {
                playerData.setValue(newValue);
            }
        }

        Set<UUID> leaderboardUuids = leaderboard.stream()
                .map(LeaderboardPlayerData::getUuid)
                .collect(Collectors.toSet());

        for (Map.Entry<UUID, Integer> entry : onlinePlayerUpdates.entrySet()) {
            if (!leaderboardUuids.contains(entry.getKey())) {
                Player player = Bukkit.getPlayer(entry.getKey());
                if (player != null) {
                    leaderboard.add(new LeaderboardPlayerData(player.getName(), entry.getKey(), kit, entry.getValue()));
                }
            }
        }

        leaderboard.sort(Comparator.comparingInt(LeaderboardPlayerData::getValue).reversed());
    }

    private int getValueForType(Profile profile, Kit kit, LeaderboardType type) {
        ProfileData data = profile.getProfileData();
        switch (type) {
            case RANKED:
                return data.getRankedKitData().getOrDefault(kit.getName(), new ProfileRankedKitData()).getElo();
            case UNRANKED:
                return data.getUnrankedKitData().getOrDefault(kit.getName(), new ProfileUnrankedKitData()).getWins();
            case FFA:
                return data.getFfaData().getOrDefault(kit.getName(), new ProfileFFAData()).getKills();
            case WIN_STREAK:
                return data.getUnrankedKitData().getOrDefault(kit.getName(), new ProfileUnrankedKitData()).getWinstreak();
            default:
                return 0;
        }
    }

    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
