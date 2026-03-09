package com.kaosmc.practice.core.database.internal;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.kaosmc.practice.bootstrap.KaosContext;
import com.kaosmc.practice.bootstrap.annotation.Service;
import com.kaosmc.practice.common.logger.Logger;
import com.kaosmc.practice.core.database.MongoService;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.SettingsLocaleImpl;
import lombok.Getter;

/**
 * @author Emmy
 * @project Kaos
 * @date 21/05/2024 - 21:40
 */
@Getter
@Service(provides = MongoService.class, priority = 30)
public class MongoServiceImpl implements MongoService {
    private final LocaleService localeService;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    /**
     * DI Constructor for the MongoServiceImpl class.
     *
     * @param localeService The locale service.
     */
    public MongoServiceImpl(LocaleService localeService) {
        this.localeService = localeService;
    }

    @Override
    public void initialize(KaosContext context) {
        String uri = this.localeService.getString(SettingsLocaleImpl.MONGO_CREDENTIALS_URI);
        String databaseName = this.localeService.getString(SettingsLocaleImpl.MONGO_CREDENTIALS_DATABASE);

        if (uri == null || uri.isEmpty() || databaseName == null || databaseName.isEmpty()) {
            Logger.error("MongoDB URI or database name is not configured in database.yml.");
            throw new IllegalStateException("MongoDB configuration is missing.");
        }

        try {
            ConnectionString connectionString = new ConnectionString(uri);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .retryWrites(true)
                    .build();

            this.mongoClient = MongoClients.create(settings);
            this.mongoDatabase = this.mongoClient.getDatabase(databaseName);

            this.mongoDatabase.listCollectionNames().first();
        } catch (Exception e) {
            Logger.error("Falha ao conectar ao MongoDB. Verifique suas credenciais e o acesso a rede.");
            throw new RuntimeException("MongoDB Connection Failure", e);
        }
    }

    @Override
    public void shutdown(KaosContext context) {
        if (this.mongoClient != null) {
            this.mongoClient.close();
            Logger.info("MongoDB connection closed.");
        }
    }
}
