package net.sadovnikov.marvinbot.core.injection;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.name.Names;
import net.sadovnikov.marvinbot.core.config.ConfigLoader;

import java.util.Locale;
import java.util.Properties;

public class ConfigInjector  extends AbstractModule {

    private ConfigLoader cfg;

    public ConfigInjector(ConfigLoader cfg) {
        this.cfg = cfg;
    }

    public void configure() {
        bind(Locale.class).toInstance(new Locale("EN"));
        bindConstant().annotatedWith(Names.named("dbHost")).to(cfg.getParam("dbHost"));
        bindConstant().annotatedWith(Names.named("dbPort")).to(cfg.getParam("dbPort"));
        bindConstant().annotatedWith(Names.named("dbName")).to(cfg.getParam("dbName"));
        bindConstant().annotatedWith(Names.named("commandPrefix")).to(cfg.getParam("commandPrefix"));
        bindConstant().annotatedWith(Names.named("admins")).to(cfg.getParam("admins"));
    }
}
