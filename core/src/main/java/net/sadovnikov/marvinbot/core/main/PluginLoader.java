package net.sadovnikov.marvinbot.core.main;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.sadovnikov.marvinbot.api.BotPluginApi;
import net.sadovnikov.marvinbot.core.db.MongoDbService;
import net.sadovnikov.marvinbot.core.schedule.TasksSchedule;
import net.sadovnikov.marvinbot.core.service.contact.ContactManager;
import net.sadovnikov.marvinbot.core.service.message.MessageSenderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.fortsoft.pf4j.*;
import net.sadovnikov.marvinbot.core.plugin.Plugin;
import ro.fortsoft.pf4j.PluginFactory;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PluginLoader {

    private PluginManager pluginManager;
    private Logger logger;
    private Injector injector;
    private Locale appLocale;


    @Inject
    public PluginLoader(Injector injector, Locale locale) {

        this.injector = injector;

        logger = LogManager.getLogger("core-logger");

        /**
         * Overriding DefaultExtensionFactory so that we could inject modules in extensions
         */
        PluginManager pluginManager = new MarvinPluginManager();
        this.appLocale = locale;

        this.pluginManager = pluginManager;
    }

    public void loadPlugins() {
        pluginManager.loadPlugins();
        pluginManager.startPlugins();

        List<String> pluginNames = new ArrayList<>();
        for (PluginWrapper plugin: pluginManager.getStartedPlugins()) {
            pluginNames.add(plugin.getPlugin().getClass().getCanonicalName());
        }

        logger.info("Run PluginManager in {} mode", pluginManager.getRuntimeMode().toString());
        if (pluginNames.size() > 0) {
            logger.info("Loaded {} plugins: {}", pluginNames.size(), String.join(", ", pluginNames));
        } else {
            logger.info("Didn't find any plugins");
        }
    }

    public PluginManager getPluginManager() {
        return pluginManager;

    }

    private class MarvinPluginFactory extends DefaultPluginFactory {
        @Override
        public Plugin create(PluginWrapper pluginWrapper) {
            Plugin plugin =
                    (Plugin) super.create(pluginWrapper);

            if (plugin != null) {
                injector.injectMembers(plugin);

                BotPluginApi pluginApiFacade = new BotPluginApi(
                        injector.getInstance(MessageSenderService.class),
                        injector.getInstance(ContactManager.class),
                        plugin.getPluginName(),
                        appLocale,
                        injector.getInstance(MongoDbService.class),
                        new TasksSchedule()
                    );

                plugin.init(pluginApiFacade);
            }

            return plugin;

        }
    }

    private class MarvinExtensionFactory extends DefaultExtensionFactory {
        @Override
        public Object create(Class<?> extensionClass) {
            logger.debug("Create instance for extension '{}'", extensionClass.getName());
            boolean isMember    = extensionClass.isMemberClass();
            boolean isNotStatic = !Modifier.isStatic(extensionClass.getModifiers());
            if (isMember && isNotStatic) {
                Class pluginClass = extensionClass.getDeclaringClass();
                try {
                    Plugin plugin = getPluginByClass(pluginClass);
                    Object extensionInstance = extensionClass.getDeclaredConstructor(pluginClass).newInstance(plugin);
                    injector.injectMembers(extensionInstance);
                    return extensionInstance;
                } catch (Exception e) {
                    logger.catching(e);
                }
            }
            return injector.getInstance(extensionClass);
        }

        public Plugin getPluginByClass(Class clazz) throws Exception {
            for (PluginWrapper wrapper : getPluginManager().getPlugins()) {
                Plugin plugin = (Plugin) wrapper.getPlugin();
                if (plugin.getClass().equals(clazz)) {
                    return plugin;
                }
            }

            throw new PluginException();
        }
    }

    private class MarvinPluginManager extends DefaultPluginManager {
        @Override
        protected ExtensionFactory createExtensionFactory() {

            return new MarvinExtensionFactory();
        }

        @Override
        protected PluginFactory createPluginFactory() {
            return new MarvinPluginFactory();
        }

    }

}
