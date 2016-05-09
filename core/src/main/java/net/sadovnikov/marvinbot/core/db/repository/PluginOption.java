package net.sadovnikov.marvinbot.core.db.repository;

import com.google.inject.Inject;
import net.sadovnikov.marvinbot.core.db.DbException;
import net.sadovnikov.marvinbot.core.db.DbService;
import net.sadovnikov.marvinbot.core.db.Executor;

import java.util.Map;

/**
 * A global plugin option
 */
public class PluginOption {

    final String HASH_BASENAME = "marvin_plugin_options";
    protected String pluginName;

    @Inject
    private DbService db;

    public PluginOption(String pluginName) {
        this.pluginName = pluginName;
    }

    public void set(String name, String value) throws DbException {
        db.exec( (Executor executor) -> executor.hset(getHashName(), name, value) );
    }

    public String get(String name) throws DbException {
        return db.exec((Executor executor) -> executor.hget(getHashName(), name));
    }

    public Map<String,String> getAll() throws DbException {
        return db.exec((Executor executor) -> executor.hgetAll(getHashName()));
    }

    protected String getHashName() {
        return HASH_BASENAME.concat(":").concat(pluginName);
    }
}
