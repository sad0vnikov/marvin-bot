package net.sadovnikov.marvinbot.core.injection;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import net.sadovnikov.marvinbot.core.db.DbService;
import net.sadovnikov.marvinbot.core.db.MongoDbService;
import net.sadovnikov.marvinbot.core.db.repository.ActiveChatsRepository;
import net.sadovnikov.marvinbot.core.db.repository.DbActiveChatsRepository;


public class Db extends AbstractModule {

    private MongoDbService dbService;

    public Db(MongoDbService dbService) {
        this.dbService = dbService;
    }

    public void configure() {
        bind(ActiveChatsRepository.class).to(DbActiveChatsRepository.class);
    }

    @Provides
    public DbService getDb() {
        return dbService;
    }
}
