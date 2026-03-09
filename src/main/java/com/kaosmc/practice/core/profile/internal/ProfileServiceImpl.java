package com.kaosmc.practice.core.profile.internal;

import com.mongodb.client.MongoCollection;
import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.bootstrap.KaosContext;
import com.kaosmc.practice.bootstrap.annotation.Service;
import com.kaosmc.practice.common.InventoryUtil;
import com.kaosmc.practice.common.logger.Logger;
import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.core.database.MongoService;
import com.kaosmc.practice.core.database.model.DatabaseProfile;
import com.kaosmc.practice.core.database.model.internal.MongoProfileImpl;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import com.kaosmc.practice.core.profile.data.ProfileData;
import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.feature.layout.data.LayoutData;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author Remi
 * @project Kaos
 * @date 5/20/2024
 */
@Getter
@Service(provides = ProfileService.class, priority = 180)
public class ProfileServiceImpl implements ProfileService {
    private final Map<UUID, Profile> profiles = new HashMap<>();
    private final MongoService mongoService;

    private MongoCollection<Document> collection;
    private DatabaseProfile databaseProfile;

    /**
     * Constructor for DI.
     */
    public ProfileServiceImpl(MongoService mongoService) {
        this.mongoService = mongoService;
    }

    @Override
    public void initialize(KaosContext context) {
        this.collection = mongoService.getMongoDatabase().getCollection("profiles");
        this.databaseProfile = new MongoProfileImpl();
    }

    @Override
    public void shutdown(KaosContext context) {
        Logger.info("Saving all loaded player profiles...");
        this.profiles.values().forEach(Profile::save);
        Logger.info("Profile saving complete.");
    }

    @Override
    public Profile getProfile(UUID uuid) {
        return this.profiles.computeIfAbsent(uuid, k -> {
            String name = KaosPractice.getInstance().getServer().getOfflinePlayer(k).getName();
            Profile profile = new Profile(k, name);
            profile.load();
            return profile;
        });
    }

    @Override
    public void removeProfile(UUID uuid) {
        this.profiles.remove(uuid);
    }

    @Override
    public void resetStats(Player player, UUID target) {
        Profile profile = this.getProfile(target);
        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);

        this.databaseProfile.archiveProfile(profile);

        profile.setProfileData(new ProfileData());
        profile.save();

        Arrays.asList(
                "",
                "&c&lRESET DE ESTATÍSTICAS",
                "&cAs estatísticas de " + targetPlayer.getName() + " foram resetadas.",
                "&7Se isso estiver sendo abusado, haverá punição.",
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));

        if (targetPlayer.isOnline() && targetPlayer.getPlayer() != null) {
            Arrays.asList(
                "",
                    "&c&lRESET DE ESTATÍSTICAS",
                    "&cSuas estatísticas foram apagadas por atividade suspeita.",
                    "&7Se você acredita que isso foi injusto, abra um ticket de suporte.",
                    ""
            ).forEach(line -> targetPlayer.getPlayer().sendMessage(CC.translate(line)));
        }
    }

    @Override
    public void resetLayoutForKit(Kit kit) {
        if (kit == null || kit.getName() == null) {
            return;
        }

        //TODO: in DOCUMENT, not only loaded profiles
        this.profiles.values().forEach(profile -> {
            if (profile == null
                    || profile.getProfileData() == null
                    || profile.getProfileData().getLayoutData() == null
                    || profile.getProfileData().getLayoutData().getLayouts() == null) {
                return;
            }

            List<LayoutData> layouts = profile.getProfileData().getLayoutData().getLayouts().get(kit.getName());
            if (layouts == null || layouts.isEmpty()) {
                profile.getProfileData().getLayoutData().addLayout(
                        kit.getName(),
                        "Layout1",
                        "Modelo 1",
                        InventoryUtil.cloneItemStackArray(kit.getItems())
                );
            } else {
                layouts.stream()
                        .filter(Objects::nonNull)
                        .forEach(layout -> layout.setItems(InventoryUtil.cloneItemStackArray(kit.getItems())));
                profile.getProfileData().getLayoutData().getLayouts().put(kit.getName(), layouts);
            }

            profile.save();
        });

        Bukkit.broadcastMessage(CC.translate("&c&lRESET DE LAYOUT: &cO layout do kit " + kit.getName() + " foi resetado para todos os jogadores."));
    }
}
