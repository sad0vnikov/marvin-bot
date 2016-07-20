package net.sadovnikov.marvinbot.core.db;

import com.mongodb.client.MongoDatabase;

public abstract class DbService {

    public abstract MongoDatabase getDb();

    public abstract MongoDatabase getDb(String databaseName);

    public void ping() throws DbException {
        try {
            getDb();
        } catch (Throwable e) {
            throw new DbException(e);
        }
    }
}
