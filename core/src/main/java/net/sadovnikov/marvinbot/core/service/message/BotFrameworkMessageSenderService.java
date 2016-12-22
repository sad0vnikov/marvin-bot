package net.sadovnikov.marvinbot.core.service.message;

import com.google.inject.Inject;
import net.sadovnikov.marvinbot.core.domain.message.MessageToSend;
import net.sadovnikov.marvinbot.core.domain.message.SentMessage;
import net.sadovnikov.mbf4j.ApiException;
import net.sadovnikov.mbf4j.Bot;
import net.sadovnikov.mbf4j.Channel;
import net.sadovnikov.mbf4j.http.Conversation;
import net.sadovnikov.mbf4j.http.HttpException;

public class BotFrameworkMessageSenderService extends MessageSenderService {

    protected Bot bot;

    @Inject
    public BotFrameworkMessageSenderService(Bot bot) {
        this.bot = bot;
    }

    @Override
    public SentMessage send(MessageToSend message) throws MessageSenderException {
        try {
            String channelId = message.chat().channel().id();
            try {
                Channel.Types channelType = Channel.Types.valueOf(channelId);
                net.sadovnikov.mbf4j.activities.outcoming.MessageToSend mbfMessage = new net.sadovnikov.mbf4j.activities.outcoming.MessageToSend(
                        new Channel(channelType),
                        new Conversation(message.chat().chatId(), "", message.chat().isGroupChat()),
                        message.text()
                );
                net.sadovnikov.mbf4j.activities.outcoming.SentMessage sentMessage = bot.messageSender().send(mbfMessage);
                return new SentMessage(sentMessage.id(), message.chat(), message.text());

            } catch (IllegalArgumentException e) {
                throw new MessageSenderException("Unknown channel type " + channelId);
            }

        } catch (ApiException | HttpException e) {
            throw new MessageSenderException(e);
        }

    }
}
