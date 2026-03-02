package com.kaosmc.practice.core.database.model.internal;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.time.DateFormat;
import com.kaosmc.practice.common.time.DateFormatter;
import com.kaosmc.practice.core.database.MongoService;
import com.kaosmc.practice.core.database.internal.MongoUtility;
import com.kaosmc.practice.core.database.model.DatabaseProfile;
import com.kaosmc.practice.core.database.model.DatabaseType;
import com.kaosmc.practice.core.profile.Profile;
import com.kaosmc.practice.core.profile.ProfileService;
import org.bson.Document;

import java.util.UUID;

/**
 * @author Remi
 * @project Kaos
 * @date 5/22/2024
 */
public class MongoProfileImpl implements DatabaseProfile {
    @Override
    public DatabaseType getType() {
        return DatabaseType.MONGO;
    }

    @Override
    public void saveProfile(Profile profile) {
        Document document = MongoUtility.toDocument(profile);
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);
        profileService.getCollection()
                .replaceOne(Filters.eq("uuid", profile.getUuid().toString()), document, new ReplaceOptions().upsert(true));
    }

    @Override
    public void loadProfile(Profile profile) {
        if (profile.getUuid() == null) return;
        ProfileService profileService = KaosPractice.getInstance().getService(ProfileService.class);

        if (profileService.getCollection() == null) return;

        Document document = profileService.getCollection().find(Filters.eq("uuid", profile.getUuid().toString())).first();
        if (document == null) {
            this.saveProfile(profile);
            return;
        }

        MongoUtility.updateProfileFromDocument(profile, document);
    }

    @Override
    public void archiveProfile(Profile profile) {
        Document archiveDocument = new Document();

        DateFormatter dateFormatter = new DateFormatter(DateFormat.DATE_PLUS_TIME, System.currentTimeMillis());
        String archiveId = UUID.randomUUID().toString();

        archiveDocument.put("archive_id", archiveId);
        archiveDocument.put("archived_at", dateFormatter.getDateFormat().format(dateFormatter.getDate()));
        archiveDocument.put("data", MongoUtility.toDocument(profile));

        MongoService mongoService = KaosPractice.getInstance().getService(MongoService.class);
        mongoService.getMongoDatabase().getCollection("profile_archives").updateOne(
                new Document("uuid", profile.getUuid().toString()),
                new Document("$push", new Document("archives", archiveDocument)),
                new UpdateOptions().upsert(true)
        );
    }
}