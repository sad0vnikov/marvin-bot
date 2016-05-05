package net.sadovnikov.marvinbot.core.plugin;

import net.sadovnikov.marvinbot.core.db.DbException;
import net.sadovnikov.marvinbot.core.db.repository.PluginOption;
import ro.fortsoft.pf4j.PluginWrapper;

import java.util.Map;

public abstract class Plugin extends ro.fortsoft.pf4j.Plugin {

    PluginOption pluginOptionDao;

    public Plugin (PluginWrapper wrapper) {
        super(wrapper);

        pluginOptionDao = new PluginOption(getPluginName());
    }

    public final String getPluginName() {
        return this.getClass().getCanonicalName();
    }

    protected final String getOption(String name) throws PluginException {
        try {
            return pluginOptionDao.get(name);
        } catch (DbException e) {
            throw new PluginException();
        }
    }

    protected final Map<String, String> getOptions() throws PluginException {
        try {
            return pluginOptionDao.getAll();
        } catch (DbException e) {
            throw new PluginException();
        }
    }

    protected final void setOption(String name, String value) throws PluginException {
        try {
            pluginOptionDao.set(name, value);
        } catch (DbException e) {
            throw new PluginException();
        }
    }

}
