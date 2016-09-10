package net.sadovnikov.marvinbot.core.plugin;

import com.google.inject.Injector;
import net.sadovnikov.marvinbot.api.BotPluginApi;
import net.sadovnikov.marvinbot.core.db.repository.GlobalPluginOption;
import net.sadovnikov.marvinbot.core.misc.Utf8Control;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.fortsoft.pf4j.PluginWrapper;

import java.util.ResourceBundle;

public abstract class Plugin extends ro.fortsoft.pf4j.Plugin {

    protected BotPluginApi marvin;

    protected Logger logger;

    protected Injector injector;

    public Plugin (PluginWrapper wrapper) {
        super(wrapper);
        logger = LogManager.getLogger("core-logger");
    }

    public final String getPluginName() {
        return this.getClass().getCanonicalName();
    }

    public ResourceBundle getLocaleBundle() {
        return ResourceBundle.getBundle("plugin_loc_data", marvin.getLocale(), this.getClass().getClassLoader(), new Utf8Control());
    }

    public void init(BotPluginApi marvin) {
        this.marvin = marvin;
    }

}
