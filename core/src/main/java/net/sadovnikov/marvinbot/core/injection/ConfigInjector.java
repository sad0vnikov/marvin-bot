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
        bindConstant().annotatedWith(Names.named("commandPrefix")).to(cfg.getParam("marvinBot.commandPrefix"));
        bindConstant().annotatedWith(Names.named("admins")).to(cfg.getParam("marvinBot.admins"));
    }
}
