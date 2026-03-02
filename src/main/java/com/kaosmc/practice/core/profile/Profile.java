package com.kaosmc.practice.core.profile;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.adapter.core.CoreAdapter;
import com.kaosmc.practice.core.profile.data.ProfileData;
import com.kaosmc.practice.core.profile.data.types.ProfileFFAData;
import com.kaosmc.practice.core.profile.data.types.ProfilePlayTimeData;
import com.kaosmc.practice.core.profile.data.types.ProfileRankedKitData;
import com.kaosmc.practice.core.profile.data.types.ProfileUnrankedKitData;
import com.kaosmc.practice.core.profile.enums.GlobalCooldown;
import com.kaosmc.practice.core.profile.enums.ProfileState;
import com.kaosmc.practice.feature.abilities.Ability;
import com.kaosmc.practice.feature.abilities.cooldown.AbilityCooldown;
import com.kaosmc.practice.feature.division.Division;
import com.kaosmc.practice.feature.division.DivisionService;
import com.kaosmc.practice.feature.division.model.DivisionTier;
import com.kaosmc.practice.feature.ffa.FFAMatch;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.kit.KitService;
import com.kaosmc.practice.feature.leaderboard.LeaderboardType;
import com.kaosmc.practice.feature.match.Match;
import com.kaosmc.practice.feature.party.Party;
import com.kaosmc.practice.feature.queue.QueueProfile;
import com.kaosmc.practice.feature.queue.QueueType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Kaos
 * @date 19/05/2024 - 22:35
 */
@Getter
@Setter
public class Profile {
    private final UUID uuid;
    private String name;
    private long firstJoin;
    private boolean online;

    private ProfileData profileData;
    private QueueProfile queueProfile;
    private ProfileState state;

    private LeaderboardType leaderboardType;
    private QueueType queueType;

    private final Map<Class<? extends Ability>, AbilityCooldown> abilityCooldowns;
    private final Map<GlobalCooldown, AbilityCooldown> globalCooldowns;

    private FFAMatch ffaMatch;
    private Match match;
    private Party party;

    private ChatColor nameColor;

    /**
     * Constructor for the Profile class.
     *
     * @param uuid The UUID of the player.
     * @param name The name of the player.
     */
    public Profile(UUID uuid, String name) {
        this.uuid = uuid;
        this.firstJoin = System.currentTimeMillis();
        this.state = ProfileState.LOBBY;
        this.profileData = new ProfileData();
        this.name = name;
        this.leaderboardType = LeaderboardType.RANKED;
        this.queueType = QueueType.UNRANKED;
        this.nameColor = ChatColor.WHITE;

        this.abilityCooldowns = new HashMap<>();
        this.globalCooldowns = new EnumMap<>(GlobalCooldown.class);
    }

    /**
     * Advanced method to retrieve the player's current color.
     * Before accessing, check if the cached color is up to date. If not, re-assign it using the CoreAdapter.
     * Logic is in place to avoid unnecessary calls to the CoreAdapter.
     *
     * @return The ChatColor representing the player's name color.
     */
    public ChatColor getNameColor() {
        CoreAdapter adapter = KaosPractice.getInstance().getService(CoreAdapter.class);
        if (adapter == null) {
            return this.nameColor;
        }

        ChatColor upToDateColor = adapter.getCore().getPlayerColor(KaosPractice.getInstance().getServer().getPlayer(this.uuid));
        if (upToDateColor == null) {
            upToDateColor = this.nameColor;
        }

        if (this.nameColor != upToDateColor) {
            this.nameColor = upToDateColor;
        }

        return this.nameColor;
    }

    /**
     * Gets the fancy name of the profile with the color.
     *
     * @return The colored name of the profile.
     */
    public String getFancyName() {
        return this.nameColor + this.name;
    }

    /**
     * Checks if the profile is currently busy with a match or FFA.
     *
     * @return True if the profile is busy, otherwise false.
     */
    public boolean isBusy() {
        return this.state != ProfileState.LOBBY;
    }

    /**
     * Checks if the profile is in the lobby or in a queue.
     *
     * @return True if the profile is in the lobby or in a queue, otherwise false.
     */
    public boolean isInLobbyOrInQueue() {
        return this.state == ProfileState.LOBBY || this.state == ProfileState.WAITING;
    }

    /**
     * Loads the profile from the database.
     */
    public void load() {
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        profileService.getDatabaseProfile().loadProfile(this);
    }

    /**
     * Saves the profile to the database.
     */
    public void save() {
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        profileService.getDatabaseProfile().saveProfile(this);
    }

    /**
     * Gets the cooldown object for a specific ability.
     * If a cooldown for this ability doesn't exist yet for this profile, it will be created.
     *
     * @param abilityClass The class of the ability (e.g., GuardianAngel.class).
     * @return The AbilityCooldown object for that ability.
     */
    public AbilityCooldown getCooldown(Class<? extends Ability> abilityClass) {
        return this.abilityCooldowns.computeIfAbsent(abilityClass, key -> new AbilityCooldown());
    }

    /**
     * Gets the cooldown object for a specific global cooldown type.
     *
     * @param type The global cooldown type from the enum.
     * @return The AbilityCooldown object.
     */
    public AbilityCooldown getGlobalCooldown(GlobalCooldown type) {
        return this.globalCooldowns.computeIfAbsent(type, key -> new AbilityCooldown());
    }

    /**
     * Retrieves a sorted list of kits that the profile has participated in
     * based on the profile's ELO for each kit, overall wins/losses and FFA kills/deaths.
     *
     * @return A sorted list of kits that the profile has participated in.
     */
    public List<Kit> getSortedKits() {
        KitService kitService = KaosPractice.getInstance().getService(KitService.class);
        return kitService.getKits()
                .stream()
                .filter(kit -> {
                    ProfileRankedKitData rankedData = this.profileData.getRankedKitData().get(kit.getName());
                    ProfileUnrankedKitData unrankedData = this.profileData.getUnrankedKitData().get(kit.getName());
                    ProfileFFAData ffaData = this.profileData.getFfaData().get(kit.getName());

                    return (rankedData != null && (rankedData.getWins() != 0 || rankedData.getLosses() != 0)) ||
                            (unrankedData != null && (unrankedData.getWins() != 0 || unrankedData.getLosses() != 0)) ||
                            (ffaData != null && (ffaData.getKills() != 0 || ffaData.getDeaths() != 0));
                })
                .sorted(Comparator.comparingInt((Kit kit) -> {
                            ProfileRankedKitData ranked = this.profileData.getRankedKitData().get(kit.getName());
                            return ranked != null ? ranked.getElo() : 0;
                        }).reversed()
                        .thenComparingInt(kit -> {
                            ProfileRankedKitData ranked = this.profileData.getRankedKitData().get(kit.getName());
                            return ranked != null ? ranked.getWins() : 0;
                        }).reversed()
                        .thenComparingInt(kit -> {
                            ProfileFFAData ffa = this.profileData.getFfaData().get(kit.getName());
                            return ffa != null ? ffa.getKills() : 0;
                        }).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Checks if the profile has participated in ranked matches.
     *
     * @return True if the profile has participated in ranked matches, otherwise false.
     */
    public boolean hasParticipatedInRanked() {
        return this.profileData.getRankedKitData().values().stream().anyMatch(data -> data.getWins() > 0 || data.getLosses() > 0 || data.getElo() != 1000);
    }

    /**
     * Checks if the profile has participated in tournaments.
     *
     * @return True if the profile has participated in tournaments, otherwise false.
     */
    public boolean hasParticipatedInTournament() {
        return false; //TODO: Implement tournament system
    }

    /**
     * Checks if the profile has participated in FFA matches.
     *
     * @return True if the profile has participated in FFA matches, otherwise false.
     */
    public boolean hasParticipatedInFFA() {
        return this.profileData.getFfaData().values().stream().anyMatch(data -> data.getKills() > 0 || data.getDeaths() > 0);
    }

    /**
     * Get the next division or tier string for a given profile and kit.
     *
     * @param kitName The name of the kit.
     * @return The next division or tier string.
     */
    public String getNextDivisionAndTier(String kitName) {
        ProfileUnrankedKitData profileUnrankedKitData = this.profileData.getUnrankedKitData().get(kitName);
        Division division = profileUnrankedKitData.getDivision();
        DivisionTier tier = profileUnrankedKitData.getTier();

        List<DivisionTier> tiers = division.getTiers();
        int tierIndex = tiers.indexOf(tier);

        if (tierIndex < tiers.size() - 1) {
            DivisionTier nextTier = tiers.get(tierIndex + 1);
            return division.getName() + " " + nextTier.getName();
        }

        DivisionService divisionService = KaosPractice.getInstance().getService(DivisionService.class);
        List<Division> divisions = divisionService.getDivisions();
        int divisionIndex = divisions.indexOf(division);

        if (divisionIndex < divisions.size() - 1) {
            Division nextDivision = divisions.get(divisionIndex + 1);
            return nextDivision.getName() + " " + nextDivision.getTiers().get(0).getName();
        }

        return profileUnrankedKitData.getDivision().getName() + " " + profileUnrankedKitData.getTier().getName();
    }

    /**
     * Get the next division for a given profile and kit.
     *
     * @param kitName The name of the kit.
     * @return The next division.
     */
    public Division getNextDivision(String kitName) {
        ProfileUnrankedKitData profileUnrankedKitData = this.profileData.getUnrankedKitData().get(kitName);
        Division division = profileUnrankedKitData.getDivision();

        DivisionService divisionService = KaosPractice.getInstance().getService(DivisionService.class);

        List<Division> divisions = divisionService.getDivisions();
        int divisionIndex = divisions.indexOf(division);

        if (divisionIndex < divisions.size() - 1) {
            return divisions.get(divisionIndex + 1);
        }

        return null;
    }

    /**
     * Updates the last play time of the profile.
     */
    public void updatePlayTime() {
        ProfilePlayTimeData playTimeData = this.profileData.getPlayTimeData();
        playTimeData.setTotal(playTimeData.getTotal() + (System.currentTimeMillis() - playTimeData.getLastLogin()));
    }
}