package net.sadovnikov.marvinbot.core.events.event_dispatchers;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.sadovnikov.marvinbot.core.domain.ChannelTypes;
import net.sadovnikov.marvinbot.core.domain.Command;
import net.sadovnikov.marvinbot.core.domain.message.ReceivedMessage;
import net.sadovnikov.marvinbot.core.events.EventDispatcher;
import net.sadovnikov.marvinbot.core.events.event_types.BotJoinedChatEvent;
import net.sadovnikov.marvinbot.core.events.event_types.BotLeftChatEvent;
import net.sadovnikov.marvinbot.core.events.event_types.MessageEvent;
import net.sadovnikov.marvinbot.core.service.CommandParser;
import net.sadovnikov.marvinbot.core.service.chat.Chat;
import net.sadovnikov.marvinbot.core.service.chat.GroupChat;
import net.sadovnikov.marvinbot.core.service.contact.BotFrameworkContactManager;
import net.sadovnikov.marvinbot.core.service.contact.ContactManager;
import net.sadovnikov.mbf4j.Address;
import net.sadovnikov.mbf4j.Bot;
import net.sadovnikov.mbf4j.Channel;
import net.sadovnikov.mbf4j.activities.incoming.ConversationUpdate;
import net.sadovnikov.mbf4j.activities.incoming.IncomingMessage;
import net.sadovnikov.mbf4j.events.EventTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BotFrameworkEventDispatcher extends EventDispatcher {

    protected net.sadovnikov.mbf4j.events.EventDispatcher bot;
    protected Injector injector;
    protected Logger logger;

    @Inject
    public BotFrameworkEventDispatcher( net.sadovnikov.mbf4j.events.EventDispatcher bot, Injector injector) {
        this.bot = bot;
        this.injector = injector;
        this.logger = LogManager.getLogger(getClass());
    }

    public void run() {
        bot.on(EventTypes.EVENT_TYPE_INCOMING_MESSAGE, (IncomingMessage event) -> {
            String chatId = event.conversation().id();
            String userName = event.from().name();
            String text = event.text().get();

            CommandParser parser = injector.getInstance(CommandParser.class);
            Command cmd = parser.parse(text);

            net.sadovnikov.marvinbot.core.domain.Channel channel = new net.sadovnikov.marvinbot.core.domain.Channel(
                ChannelTypes.valueOf(event.channel().type().name())
            );
            ReceivedMessage message = new ReceivedMessage(new Chat(channel, event.conversation().id()), userName, text, cmd);
            logger.info("new message from " + chatId + ": " + message.text());

            dispatch(new MessageEvent(message));

        }).on(EventTypes.EVENT_TYPE_CONVERSATION_UPDATE, (ConversationUpdate event) -> {
            List<Address> membersRemoved = event.membersRemoved();
            List<Address> membersAdded   = event.membersRemoved();
            Address recepient = event.recipient();
            Channel mbfChannel = event.channel();
            net.sadovnikov.marvinbot.core.domain.Channel channel = new net.sadovnikov.marvinbot.core.domain.Channel(
                    ChannelTypes.valueOf(mbfChannel.type().name()));

            Chat chat = new Chat(channel, event.conversation().id());

            if (event.conversation().isGroup()) {
                chat = new GroupChat(channel, event.conversation().id());
            }
            logger.info("new conversationUpdateEvent from " + event.conversation().id());


            /**
             * Bot's username is different for every channel and we can't know it advance.
             * So, we'll try to get it from RECEPIENT field of message.
             * Than the bot needs to remember he belongs to this chat, since there is not way to get active chats list from API
             */
            BotFrameworkContactManager contactManager = injector.getInstance(BotFrameworkContactManager.class);
            if (membersAdded.contains(recepient)) {
                dispatch(new BotJoinedChatEvent(chat));
                logger.info("bot joined chat " + chat.chatId());
            }

            if (membersRemoved.contains(recepient)) {
                dispatch(new BotLeftChatEvent(chat));
                logger.info("marked left chat " + chat.chatId());
            }

        });

    }
}
