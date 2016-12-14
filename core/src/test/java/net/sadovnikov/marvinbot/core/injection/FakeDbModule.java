package net.sadovnikov.marvinbot.core.injection;

import com.google.inject.AbstractModule;
import net.sadovnikov.marvinbot.core.db.DbService;
import net.sadovnikov.marvinbot.core.db.FakeDbService;
import net.sadovnikov.marvinbot.core.db.MongoDbService;
import net.sadovnikov.marvinbot.core.db.repository.ActiveChatsRepository;
import net.sadovnikov.marvinbot.core.db.repository.DbActiveChatsRepository;

public class FakeDbModule extends AbstractModule {

    private MongoDbService dbService;

    public void configure() {
        bind(DbService.class).toInstance(new FakeDbService());
        bind(ActiveChatsRepository.class).to(DbActiveChatsRepository.class);
    }
}
