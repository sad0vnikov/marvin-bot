package net.sadovnikov.marvinbot.core.db;


import com.github.fakemongo.Fongo;
import com.mongodb.client.MongoDatabase;

public class FakeDbService extends DbService {

    private Fongo fongo;

    public FakeDbService() {
        super();
        this.fongo = new Fongo("fongo server");
    }

    @Override
    public MongoDatabase getDb() {
        return fongo.getDatabase("fakedb");
    }

    @Override
    public MongoDatabase getDb(String databaseName) {
        return fongo.getDatabase(databaseName);
    }
}
