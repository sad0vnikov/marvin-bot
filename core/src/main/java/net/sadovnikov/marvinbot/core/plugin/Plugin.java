package net.sadovnikov.marvinbot.core.plugin;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.sadovnikov.marvinbot.core.db.DbException;
import net.sadovnikov.marvinbot.core.db.repository.PluginOption;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.fortsoft.pf4j.PluginWrapper;

import java.util.Map;

public abstract class Plugin extends ro.fortsoft.pf4j.Plugin {

    private PluginOption pluginOptionRepository;
    private Logger logger;

    protected Injector injector;

    public Plugin (PluginWrapper wrapper) {
        super(wrapper);
        logger = LogManager.getLogger("core-logger");
    }

    public final String getPluginName() {
        return this.getClass().getCanonicalName();
    }

    protected final String getOption(String name, String defaultValue) throws PluginException {
        try {
            String value = getPluginOptionRepository().get(name);
            if (value != null) {
                return value;
            }

            return defaultValue;

        } catch (DbException e) {
            throw new PluginException(e);
        }

    }

    protected final Map<String, String> getOptions() throws PluginException {
        try {
            return getPluginOptionRepository().getAll();
        } catch (DbException e) {
            throw new PluginException(e);

        }
    }

    protected final void setOption(String name, String value) throws PluginException {
        try {
            getPluginOptionRepository().set(name, value);
        } catch (DbException e) {
            throw new PluginException(e);
        }
    }

    @Inject
    protected void setInjector(Injector injector) {
        this.injector = injector;
    }

    protected final PluginOption getPluginOptionRepository() {

        if (pluginOptionRepository == null) {
            pluginOptionRepository = new PluginOption(getPluginName());
            injector.injectMembers(pluginOptionRepository);
        }

        return pluginOptionRepository;
    }

}
