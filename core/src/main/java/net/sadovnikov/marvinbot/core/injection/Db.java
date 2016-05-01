package net.sadovnikov.marvinbot.core.injection;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import net.sadovnikov.marvinbot.core.db.DbService;


public class Db extends AbstractModule {

    public void configure() {
        bind(DbService.class).in(Singleton.class);
    }
}
