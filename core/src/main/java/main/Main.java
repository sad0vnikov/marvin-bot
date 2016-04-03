package main;


import config.ConfigException;
import config.ConfigLoader;
import events.EventDispatcher;
import events.eventListeners.MessageEventListener;
import events.eventTypes.MessageEvent;
import factories.Skype4jFactory;
import factories.SkypeFactory;
import messageSender.MessageSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.fortsoft.pf4j.DefaultPluginManager;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginManager;
import ro.fortsoft.pf4j.PluginWrapper;

import java.util.ArrayList;
import java.util.List;

public class Main {

    final static String configPath = "config/connection.properties";

    public static void main(String[] args) {

        Logger logger = LogManager.getLogger("core-logger");

        PluginManager pluginManager = loadPlugins(logger);

        ConfigLoader config;
        try {
            config = new ConfigLoader(configPath);
            logger.info("loaded config from " + configPath);
        } catch (ConfigException e) {
            logger.error(e.getMessage());
            return;
        }

        String skypeLogin    = config.getParam("login");
        String skypePassword = config.getParam("password");
        SkypeFactory skypeFactory = new Skype4jFactory(skypeLogin, skypePassword);
        logger.info("logged in successfully (skype login " + skypeLogin +  ")");
        EventDispatcher dispatcher = skypeFactory.getEventDispatcher();
        MessageSender sender       = skypeFactory.getMessageSender();
        dispatcher.addListener(MessageEvent.class.getTypeName(), new MessageEventListener(pluginManager, sender));

        dispatcher.start();
        logger.info("waiting for events...");
        try {
            dispatcher.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private static PluginManager loadPlugins(Logger logger) {
        PluginManager pluginManager = new DefaultPluginManager();
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
