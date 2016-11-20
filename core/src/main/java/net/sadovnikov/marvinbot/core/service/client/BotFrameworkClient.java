package net.sadovnikov.marvinbot.core.service.client;

import net.sadovnikov.mbf4j.Bot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class BotFrameworkClient extends Client {

    protected Bot bot;
    protected Logger logger = LogManager.getLogger(getClass());

    public BotFrameworkClient(String appId, String secret) {
        bot = new Bot();
        bot
                .setCredentials(appId, secret);
    }

    @Override
    public void connect() throws ClientException {
        try {
            bot.init();
        } catch (IOException e) {
            logger.catching(e);
        }
    }

    @Override
    public void disconnect() throws ClientException {
        // stopping bot framework http server is not implemented yet
    }

    public Bot getBotInstance() {
        return bot;
    }
}
