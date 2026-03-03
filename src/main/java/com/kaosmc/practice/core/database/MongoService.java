package com.kaosmc.practice.core.database;

import com.mongodb.client.MongoDatabase;
import com.kaosmc.practice.bootstrap.lifecycle.Service;

/**
 * @author Remi
 * @project kaos-practice
 * @date 2/07/2025
 */
public interface MongoService extends Service {
    /**
     * Gets the active MongoDatabase instance for other services to use.
     *
     * @return The MongoDatabase instance.
     * @throws IllegalStateException if the service has not been initialized yet.
     */
    MongoDatabase getMongoDatabase();
}