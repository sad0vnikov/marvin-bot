package net.sadovnikov.marvinbot.core.injection;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import net.sadovnikov.marvinbot.core.main.PluginLoader;


public class PluginManagerInjector extends AbstractModule {

    public void configure() {
        bind(PluginLoader.class).in(Singleton.class);
    }
}
