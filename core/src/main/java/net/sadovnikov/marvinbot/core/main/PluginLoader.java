package net.sadovnikov.marvinbot.core.main;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.sadovnikov.marvinbot.core.db.DbException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.*;
import ro.fortsoft.pf4j.*;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginFactory;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class PluginLoader {

    private PluginManager pluginManager;
    private Logger logger;
    private Injector injector;


    @Inject
    public PluginLoader(Injector injector) {

        this.injector = injector;

        logger = LogManager.getLogger("core-logger");

        /**
         * Overriding DefaultExtensionFactory so that we could inject modules in extensions
         */
        PluginManager pluginManager = new DefaultPluginManager() {
            @Override
            protected ExtensionFactory createExtensionFactory() {

                return new GuiceExtensionFactory();
            }

            @Override
            protected PluginFactory createPluginFactory() {
                return new GuicePluginFactory();
            }
        };

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

    private class GuicePluginFactory extends DefaultPluginFactory {
        @Override
        public Plugin create(PluginWrapper pluginWrapper) {
            Plugin plugin = super.create(pluginWrapper);
            if (plugin != null) {
                injector.injectMembers(plugin);
            }

            return plugin;
        }
    }


    private class GuiceExtensionFactory extends DefaultExtensionFactory {
        @Override
        public Object create(Class<?> extensionClass) {
            logger.debug("Create instance for extension '{}'", extensionClass.getName());
            boolean isMember    = extensionClass.isMemberClass();
            boolean isNotStatic = !Modifier.isStatic(extensionClass.getModifiers());
            if (isMember && isNotStatic) {
                Class pluginClass = extensionClass.getDeclaringClass();
                try {
                    Plugin plugin = getPluginByClass(pluginClass);
                    return extensionClass.getDeclaredConstructor(pluginClass).newInstance(plugin);
                } catch (Exception e) {
                    logger.catching(e);
                }
            }
            return injector.getInstance(extensionClass);
        }

        public Plugin getPluginByClass(Class clazz) throws Exception {
            for (PluginWrapper wrapper : getPluginManager().getPlugins()) {
                Plugin plugin = wrapper.getPlugin();
                if (plugin.getClass().equals(clazz)) {
                    return plugin;
                }
            }

            throw new PluginException();
        }
    }



}
