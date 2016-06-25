package net.sadovnikov.marvinbot.core.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private Properties prop;
    private Logger logger;

    public ConfigLoader(String fileName) throws ConfigException {

        logger = LogManager.getLogger("core-logger");
        logger.debug(System.getenv());
        prop = new Properties();

        try (InputStream inputStream = new FileInputStream(fileName)) {
            prop.load(inputStream);
            logger.debug("loaded config file " + fileName);
        } catch (IOException e) {
            throw new ConfigException("net.sadovnikov.marvinbot.core.config file " + fileName + "couldn\'t be loaded");
        }

    }

    public String getParam(String name)
    {

        String value = System.getProperty(name); // every config param can be overridden by system property
        logger.debug("reading property " + name + " from system properties");

        if (value == null) {
            String envName = name.toUpperCase().replace(".", "_");
            logger.debug("reading property " + envName + " from env");
            value = System.getenv(envName); // or by env param
        }


        if (value == null) {
            logger.debug("reading property " + name + " from config file");
            value = prop.getProperty(name);
        }

        logger.debug("property " + name + " = " + value);
        return value;
    }


}
