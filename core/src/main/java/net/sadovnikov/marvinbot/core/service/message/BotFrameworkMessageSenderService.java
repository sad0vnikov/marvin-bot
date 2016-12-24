package net.sadovnikov.marvinbot.core.service.message;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.sadovnikov.marvinbot.core.domain.message.MessageToSend;
import net.sadovnikov.marvinbot.core.domain.message.SentMessage;
import net.sadovnikov.marvinbot.core.service.botframework.BotSelfAddressCachingService;
import net.sadovnikov.mbf4j.Address;
import net.sadovnikov.mbf4j.ApiException;
import net.sadovnikov.mbf4j.Bot;
import net.sadovnikov.mbf4j.Channel;
import net.sadovnikov.mbf4j.http.Conversation;
import net.sadovnikov.mbf4j.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class BotFrameworkMessageSenderService extends MessageSenderService {

    protected Bot bot;
    protected Injector injector;
    protected BotSelfAddressCachingService botAddressCachingService;

    @Inject
    public BotFrameworkMessageSenderService(Bot bot, Injector injector) {
        this.bot = bot;
        this.injector = injector;
        this.botAddressCachingService = injector.getInstance(BotSelfAddressCachingService.class);
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
                Optional<String> botId = botAddressCachingService.getBotIdForChannel(message.chat().channel());
                if (!botId.isPresent()) {
                    throw new MessageSenderException("cannot find bot id for channel " + message.chat().channel().id());
                }
                Address from = new Address(botId.get(), "");
                mbfMessage.withFrom(from);
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
