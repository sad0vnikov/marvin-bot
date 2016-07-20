package net.sadovnikov.marvinbot.core.injection;

import com.google.inject.AbstractModule;
import net.sadovnikov.marvinbot.core.db.MongoDbService;


public class Db extends AbstractModule {

    private MongoDbService dbService;

    public Db(MongoDbService dbService) {
        this.dbService = dbService;
    }

    public void configure() {
        bind(MongoDbService.class).toInstance(dbService);
    }
}
