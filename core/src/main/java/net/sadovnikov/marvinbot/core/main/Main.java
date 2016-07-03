package net.sadovnikov.marvinbot.core.main;


import net.sadovnikov.marvinbot.core.injection.LoggerInjector;
import net.sadovnikov.marvinbot.core.service.client.Skype4jClient;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.sadovnikov.marvinbot.core.config.ConfigException;
import net.sadovnikov.marvinbot.core.config.ConfigLoader;
import net.sadovnikov.marvinbot.core.db.DbService;
import net.sadovnikov.marvinbot.core.events.EventDispatcher;
import net.sadovnikov.marvinbot.core.injection.PluginManagerInjector;
import net.sadovnikov.marvinbot.core.injection.ConfigInjector;
import net.sadovnikov.marvinbot.core.injection.Skype4jInjector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    final static String configPath = "config/main.properties";

    public static void main(String[] args) {

        Logger logger = LogManager.getLogger("core-logger");


        ConfigLoader config;
        logger.debug("loging config at " + configPath);
        try {
            config = new ConfigLoader(configPath);
            logger.info("loaded net.sadovnikov.marvinbot.core.config from " + configPath);
        } catch (ConfigException e) {
            logger.error(e.getMessage());
            System.exit(2);
            return;
        }

        DbService dbService;
        logger.debug("connecting to db");
        try {
            String dbHost = config.getParam("marvinBot.dbHost");
            String dbPort = config.getParam("marvinBot.dbPort");
            String dbName = config.getParam("marvinBot.dbName");
            logger.debug("db host: " + dbHost);
            logger.debug("db dbPort: " + dbPort);
            logger.debug("using db: " + dbName);

            dbService = new DbService(dbHost, dbPort, dbName);
            dbService.ping();
            logger.info("Connected to db on " + dbHost + ":" + dbPort);
        } catch (Throwable e) {
            logger.error("Cannot connect to db");
            logger.catching(e);
            System.exit(2);
            return;
        }

        String skypeLogin    = config.getParam("marvinBot.login");
        String skypePassword = config.getParam("marvinBot.password");

        logger.debug("connecting to Skype using login: " + skypeLogin);

        Skype4jClient skypeClient = new Skype4jClient(skypeLogin, skypePassword);
        Injector injector = Guice.createInjector(
                new Skype4jInjector(skypeClient.getSkype4jInstance()),
                new PluginManagerInjector(),
                new ConfigInjector(config),
                new net.sadovnikov.marvinbot.core.injection.Db(dbService),
                new LoggerInjector(logger)
        );

        skypeClient.connect();
        skypeClient.setVisible();

        logger.info("logged in successfully (skype login " + skypeLogin +  ")");

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
