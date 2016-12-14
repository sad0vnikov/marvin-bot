package net.sadovnikov.marvinbot.core.events.event_dispatchers;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.sadovnikov.marvinbot.core.domain.Command;
import net.sadovnikov.marvinbot.core.domain.message.ReceivedMessage;
import net.sadovnikov.marvinbot.core.events.EventDispatcher;
import net.sadovnikov.marvinbot.core.events.event_types.MessageEvent;
import net.sadovnikov.marvinbot.core.service.CommandParser;
import net.sadovnikov.marvinbot.core.service.contact.BotFrameworkContactManager;
import net.sadovnikov.marvinbot.core.service.contact.ContactManager;
import net.sadovnikov.mbf4j.Address;
import net.sadovnikov.mbf4j.Bot;
import net.sadovnikov.mbf4j.Channel;
import net.sadovnikov.mbf4j.activities.incoming.ConversationUpdate;
import net.sadovnikov.mbf4j.activities.incoming.IncomingMessage;
import net.sadovnikov.mbf4j.events.EventTypes;

import java.util.List;

public class BotFrameworkEventDispatcher extends EventDispatcher {

    protected net.sadovnikov.mbf4j.events.EventDispatcher bot;
    protected Injector injector;

    @Inject
    public BotFrameworkEventDispatcher( net.sadovnikov.mbf4j.events.EventDispatcher bot, Injector injector) {
        this.bot = bot;
        this.injector = injector;
    }

    public void run() {

        bot.on(EventTypes.EVENT_TYPE_INCOMING_MESSAGE, (IncomingMessage event) -> {
            String chatId = event.conversation().id();
            String userName = event.from().name();
            String text = event.text().get();

            CommandParser parser = injector.getInstance(CommandParser.class);
            Command cmd = parser.parse(text);

            ReceivedMessage message = new ReceivedMessage(chatId, userName, text, cmd);
            dispatch(new MessageEvent(message));

        }).on(EventTypes.EVENT_TYPE_CONVERSATION_UPDATE, (ConversationUpdate event) -> {
            List<Address> membersRemoved = event.membersRemoved();
            List<Address> membersAdded   = event.membersRemoved();
            Address recepient = event.recipient();
            Channel channel = event.channel();

            /**
             * Bot's username is different for every channel and we can't know it advance.
             * So, we'll try to get it from RECEPIENT field of message.
             * Than the bot needs to remember he belongs to this chat, since there is not way to get active chats list from API
             */
            BotFrameworkContactManager contactManager = injector.getInstance(BotFrameworkContactManager.class);
        });

    }
}
