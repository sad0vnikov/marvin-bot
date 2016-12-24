package net.sadovnikov.marvinbot.core.main;


import net.sadovnikov.marvinbot.core.injection.*;
import net.sadovnikov.marvinbot.core.service.client.BotFrameworkClient;
import net.sadovnikov.marvinbot.core.service.client.Skype4jClient;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.sadovnikov.marvinbot.core.config.ConfigException;
import net.sadovnikov.marvinbot.core.config.ConfigLoader;
import net.sadovnikov.marvinbot.core.db.MongoDbService;
import net.sadovnikov.marvinbot.core.events.EventDispatcher;
import net.sadovnikov.marvinbot.core_event_handlers.BotFrameworkBotAddressCacheManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    final static String configPath = "config/main.properties";

    public static void main(String[] args) {

        Logger logger = LogManager.getLogger("core-logger");


        ConfigLoader config;
        logger.debug("loading config at " + configPath);
        try {
            config = new ConfigLoader(configPath);
            logger.info("loaded net.sadovnikov.marvinbot.core.config from " + configPath);
        } catch (ConfigException e) {
            logger.error(e.getMessage());
            System.exit(2);
            return;
        }

        MongoDbService dbService;
        logger.debug("connecting to db");
        try {
            String dbHost = config.getParam("marvinBot.dbHost");
            String dbPort = config.getParam("marvinBot.dbPort");
            String dbName = config.getParam("marvinBot.dbName");
            logger.debug("db host: " + dbHost);
            logger.debug("db dbPort: " + dbPort);
            logger.debug("using db: " + dbName);

            dbService = new MongoDbService(dbHost, dbPort, dbName);
            dbService.ping();
            logger.info("Connected to db on " + dbHost + ":" + dbPort);
        } catch (Throwable e) {
            logger.error("Cannot connect to db");
            logger.catching(e);
            System.exit(2);
            return;
        }

        String appId     = config.getParam("marvinBot.botframework.appId");
        String appSecret = config.getParam("marvinBot.botframework.secret");
        String isEmulatorParam = config.getParam("marvinBot.botframework.emulator");
        boolean isEmulator = false;
        if (isEmulatorParam != null && isEmulatorParam.equals("true")) {
            isEmulator = true;
        }


        BotFrameworkClient client = new BotFrameworkClient(appId, appSecret);

        if (isEmulator) {
            String emulatorHost = config.getParam("marvinBot.botframework.emulator.host");
            int emulatorPort = Integer.valueOf(config.getParam("marvinBot.botframework.emulator.port"));
            client.useEmulator(emulatorHost, emulatorPort);
        }

        Injector injector = Guice.createInjector(
                new BotFrameworkInjector(client.getBotInstance()),
                new PluginManagerInjector(),
                new ConfigInjector(config),
                new net.sadovnikov.marvinbot.core.injection.Db(dbService),
                new LoggerInjector(logger)
        );

        //every outcoming bot event needs to have 'from' field with bot id
        //but there is no way to get bot id in advance, so we set up a component which will get bot id from incoming events
        //and cache it
        BotFrameworkBotAddressCacheManager botAddressCaching = injector.getInstance(BotFrameworkBotAddressCacheManager.class);
        botAddressCaching.initCaching(client);

        client.connect();

        logger.info("logged in successfully (skype login " + appId +  ")");

        EventDispatcher dispatcher = injector.getInstance(EventDispatcher.class);
        injector.getInstance(PluginLoader.class).loadPlugins();

        dispatcher.start();
        logger.info("waiting for net.sadovnikov.marvinbot.core.events...");
        try {
            dispatcher.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
