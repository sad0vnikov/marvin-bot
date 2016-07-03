package net.sadovnikov.marvinbot.core.injection;

import com.google.inject.AbstractModule;
import org.apache.logging.log4j.Logger;

public class LoggerInjector extends AbstractModule {

    protected Logger logger;

    public LoggerInjector(Logger logger) {
        this.logger = logger;
    }

    @Override
    protected void configure() {
        bind(Logger.class).toInstance(logger);
    }
}
