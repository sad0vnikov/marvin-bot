package net.sadovnikov.marvinbot.core.db;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;


public class DbService {

    protected MongoClient client;
    protected String databaseName;

    @Inject
    public DbService(@Named("dbHost") String dbHost, @Named("dbPort") String dbPort) {
        this.client = new MongoClient(dbHost, Integer.parseInt(dbPort));
    }

    @Inject
    protected void setDatabaseName(@Named("dbName") String dbName) {
        this.databaseName = dbName;
    }

    public MongoDatabase getDb() {
        return client.getDatabase(databaseName);
    }
}
