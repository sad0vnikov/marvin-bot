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

    protected static final String DEFAULT_LOCALE = "EN";

    public void configure() {
        String locale = DEFAULT_LOCALE;
        String configLocale = cfg.getParam("marvinBot.locale");
        if (configLocale != null) {
            locale = configLocale;
        }
        bind(Locale.class).  toInstance(new Locale(locale));
        bindConstant().annotatedWith(Names.named("commandPrefix")).to(cfg.getParam("marvinBot.commandPrefix"));
        bindConstant().annotatedWith(Names.named("admins")).to(cfg.getParam("marvinBot.admins"));
    }
}
