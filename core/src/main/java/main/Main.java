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

public class Main {

    final static String configPath = "config/connection.properties";

    public static void main(String[] args) {

        Logger logger = LogManager.getLogger("core-logger");

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
        dispatcher.addListener(MessageEvent.class.getTypeName(), new MessageEventListener(sender));

        dispatcher.start();
        logger.info("waiting for events...");
        try {
            dispatcher.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
