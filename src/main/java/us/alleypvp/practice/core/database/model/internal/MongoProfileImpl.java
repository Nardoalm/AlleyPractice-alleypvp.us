package us.alleypvp.practice.core.database.model.internal;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.common.time.DateFormat;
import us.alleypvp.practice.common.time.DateFormatter;
import us.alleypvp.practice.core.database.MongoService;
import us.alleypvp.practice.core.database.internal.MongoUtility;
import us.alleypvp.practice.core.database.model.DatabaseProfile;
import us.alleypvp.practice.core.database.model.DatabaseType;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.ProfileService;
import org.bson.Document;

import java.util.UUID;

/**
 * @author Remi
 * @project Alley
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
        ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);
        profileService.getCollection()
                .replaceOne(Filters.eq("uuid", profile.getUuid().toString()), document, new ReplaceOptions().upsert(true));
    }

    @Override
    public void loadProfile(Profile profile) {
        if (profile.getUuid() == null) return;
        ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);

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

        MongoService mongoService = AlleyPractice.getInstance().getService(MongoService.class);
        mongoService.getMongoDatabase().getCollection("profile_archives").updateOne(
                new Document("uuid", profile.getUuid().toString()),
                new Document("$push", new Document("archives", archiveDocument)),
                new UpdateOptions().upsert(true)
        );
    }
}