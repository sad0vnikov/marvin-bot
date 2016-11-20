package net.sadovnikov.marvinbot.core.service.message;

import com.google.inject.Inject;
import net.sadovnikov.marvinbot.core.domain.message.MessageToSend;
import net.sadovnikov.marvinbot.core.domain.message.SentMessage;
import net.sadovnikov.mbf4j.Bot;

public class BotFrameworkMessageSenderService extends MessageSenderService {

    protected Bot bot;

    @Inject
    public BotFrameworkMessageSenderService(Bot bot) {
        this.bot = bot;
    }

    @Override
    public SentMessage send(MessageToSend message) throws MessageSenderException {
        return null;
    }
}
