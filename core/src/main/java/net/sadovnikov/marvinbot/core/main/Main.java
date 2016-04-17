package net.sadovnikov.marvinbot.core.main;


import net.sadovnikov.marvinbot.core.client.Skype4jClient;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.sadovnikov.marvinbot.core.config.ConfigException;
import net.sadovnikov.marvinbot.core.config.ConfigLoader;
import net.sadovnikov.marvinbot.core.events.EventDispatcher;
import net.sadovnikov.marvinbot.core.injection.Skype4jInjector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.fortsoft.pf4j.*;

import java.util.ArrayList;
import java.util.List;

public class Main {

    final static String configPath = "config/connection.properties";

    public static void main(String[] args) {

        Logger logger = LogManager.getLogger("core-logger");


        ConfigLoader config;
        try {
            config = new ConfigLoader(configPath);
            logger.info("loaded net.sadovnikov.marvinbot.core.config from " + configPath);
        } catch (ConfigException e) {
            logger.error(e.getMessage());
            return;
        }


        String skypeLogin    = config.getParam("login");
        String skypePassword = config.getParam("password");

        Skype4jClient skypeClient = new Skype4jClient(skypeLogin, skypePassword);
        Injector injector = Guice.createInjector(new Skype4jInjector(skypeClient.getSkype4jInstance()));

        skypeClient.connect();
        skypeClient.setVisible();

        logger.info("logged in successfully (skype login " + skypeLogin +  ")");

        EventDispatcher dispatcher = injector.getInstance(EventDispatcher.class);
        PluginManager pluginManager = loadPlugins(logger, injector);
        dispatcher.setPluginManager(pluginManager);

        dispatcher.start();
        logger.info("waiting for net.sadovnikov.marvinbot.core.events...");
        try {
            dispatcher.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private static PluginManager loadPlugins(Logger logger, Injector injector) {

        /**
         * Overriding DefaultExtensionFactory so that we could inject modules in extensions
         */
        PluginManager pluginManager = new DefaultPluginManager() {
            @Override
            protected ExtensionFactory createExtensionFactory() {
                return new DefaultExtensionFactory() {
                    @Override
                    public Object create(Class<?> extensionClass) {
                        logger.debug("Create instance for extension '{}'", extensionClass.getName());
                        return injector.getInstance(extensionClass);
                    }
                };
            }
        };

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

        return pluginManager;
    }

}
