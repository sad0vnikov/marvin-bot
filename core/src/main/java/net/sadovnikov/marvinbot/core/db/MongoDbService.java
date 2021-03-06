package net.sadovnikov.marvinbot.core.db;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;


public class MongoDbService extends DbService {

    protected MongoClient client;
    protected String defaultDb;

    @Inject
    public MongoDbService(@Named("dbHost") String dbHost, @Named("dbPort") String dbPort, @Named("dbName") String defaultDb) {
        this.client = new MongoClient(dbHost, Integer.parseInt(dbPort));
        this.defaultDb = defaultDb;
    }


    public MongoDatabase getDb() {
        return client.getDatabase(defaultDb);
    }

    public MongoDatabase getDb(String databaseName) {
        return client.getDatabase(databaseName);
    }

}
