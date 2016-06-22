package net.sadovnikov.marvinbot.core.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private Properties prop;

    public ConfigLoader(String fileName) throws ConfigException {

        prop = new Properties();

        try (InputStream inputStream = new FileInputStream(fileName)) {
            prop.load(inputStream);
        } catch (IOException e) {
            throw new ConfigException("net.sadovnikov.marvinbot.core.config file " + fileName + "couldn\'t be loaded");
        }

    }

    public String getParam(String name)
    {

        String value = System.getProperty(name); // every config param can be overridden by system property

        if (value == null) {
            value = System.getenv(name); // or by env param
        }


        if (value == null) {
            value = prop.getProperty(name);
        }
        return value;
    }


}
