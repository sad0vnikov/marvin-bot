package net.sadovnikov.marvinbot.core.plugin;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.sadovnikov.marvinbot.core.db.DbException;
import net.sadovnikov.marvinbot.core.db.repository.PluginChatOption;
import net.sadovnikov.marvinbot.core.db.repository.GlobalPluginOption;
import net.sadovnikov.marvinbot.core.db.repository.PluginOption;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.ParseException;
import ro.fortsoft.pf4j.PluginWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Plugin extends ro.fortsoft.pf4j.Plugin {

    private GlobalPluginOption globalPluginOptionRepository;
    protected Logger logger;

    protected Injector injector;

    public Plugin (PluginWrapper wrapper) {
        super(wrapper);
        logger = LogManager.getLogger("core-logger");
    }

    public final String getPluginName() {
        return this.getClass().getCanonicalName();
    }

    protected final String getGlobalOption(String name, String defaultValue) throws PluginException {
        try {
            String value = getGlobalPluginOptionRepository().get(name);
            if (value != null) {
                return value;
            }

            return defaultValue;

        } catch (DbException e) {
            throw new PluginException(e);
        }

    }

    protected final Map<String, String> getGlobalOptions() throws PluginException {
        try {
            return getGlobalPluginOptionRepository().getAll();
        } catch (DbException e) {
            throw new PluginException(e);

        }
    }

    protected final void setGlobalOption(String name, Object value) throws PluginException {
        try {
            getGlobalPluginOptionRepository().set(name, value);
        } catch (DbException e) {
            throw new PluginException(e);
        }
    }

    protected final String getChatOption(String chatId, String name, String defaultValue) throws PluginException {
        try {
            String value = getPluginChatOptionRepository(chatId).get(name);
            if (value != null) {
                return value;
            }

            return defaultValue;

        } catch (DbException e) {
            throw new PluginException(e);
        }
    }

    protected final List<String> getListChatOption(String chatId, String name) throws PluginException {

        try {
            List<String> value = getPluginChatOptionRepository(chatId).getValuesList(name);

            if (value != null) {
                return value;
            }

            return new ArrayList<>();
        } catch (DbException e) {
            throw new PluginException(e);
        }
    }

    protected final Set<String> findChatswithOptionValues(String optionName, String optionValues) throws PluginException {
        return getPluginChatOptionRepository(null).findChatswithOptionValues(optionName, optionValues);

    }

    protected final Map<String,String> getChatOptions(String chatId, String name) throws PluginException {
        try {
            return getPluginChatOptionRepository(chatId).getAll();

        } catch (DbException e) {
            throw new PluginException(e);
        }
    }

    protected final void setChatOption(String chatId, String name, Object value) throws PluginException {
        try {
            getPluginChatOptionRepository(chatId).set(name, value);
        } catch (DbException e) {
            throw new PluginException(e);
        }
    }

    @Inject
    protected void setInjector(Injector injector) {
        this.injector = injector;
    }

    protected final GlobalPluginOption getGlobalPluginOptionRepository() {

        if (globalPluginOptionRepository == null) {
            globalPluginOptionRepository = new GlobalPluginOption(getPluginName());
            injector.injectMembers(globalPluginOptionRepository);
        }

        return globalPluginOptionRepository;
    }

    protected final PluginChatOption getPluginChatOptionRepository(String chatId) {
        PluginChatOption pluginChatOption = new PluginChatOption(chatId, getPluginName());
        injector.injectMembers(pluginChatOption);
        return pluginChatOption;
    }

}
