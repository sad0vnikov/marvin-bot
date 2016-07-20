package net.sadovnikov.marvinbot.core.events.event_dispatchers;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.chat.messages.ReceivedMessage;
import com.samczsun.skype4j.events.EventHandler;
import com.samczsun.skype4j.events.Listener;
import com.samczsun.skype4j.events.chat.ChatJoinedEvent;
import com.samczsun.skype4j.events.chat.ChatQuitEvent;
import com.samczsun.skype4j.events.chat.message.MessageReceivedEvent;
import com.samczsun.skype4j.exceptions.ConnectionException;
import net.sadovnikov.marvinbot.core.domain.Command;
import net.sadovnikov.marvinbot.core.domain.Contact;
import net.sadovnikov.marvinbot.core.events.EventDispatcher;
import net.sadovnikov.marvinbot.core.events.event_types.*;
import net.sadovnikov.marvinbot.core.service.CommandParser;
import org.apache.logging.log4j.LogManager;

/**
 * An EventDispatcher implementation whilch is a wrapper for Skype4j event dispatcher.
 * Dispatches any kind of skype net.sadovnikov.marvinbot.core.events
 */
public class Skype4jEventDispatcher extends EventDispatcher {

    Skype skype;
    Injector injector;


    @Inject
    public Skype4jEventDispatcher(Skype skype, Injector injector) {
        this.skype = skype;
        this.injector = injector;
    }


    public void run() {

        this.skype.getEventDispatcher().registerListener(new Listener() {

            @EventHandler
            public void onMessage(MessageReceivedEvent ev) {
                ReceivedMessage skype4jMessage = ev.getMessage();
                String msgContent = skype4jMessage.getContent().asPlaintext();
                skype4jMessage.getChat().getIdentity();
                String chatId = skype4jMessage.getChat().getIdentity();

                LogManager.getLogger("core-logger").info("new net.sadovnikov.marvinbot.core.domain.message from " + chatId + ": " + msgContent);

                String userName = skype4jMessage.getSender().getUsername();

                CommandParser parser = injector.getInstance(CommandParser.class);
                Command cmd = parser.parse(msgContent);

                net.sadovnikov.marvinbot.core.domain.message.ReceivedMessage msg = new net.sadovnikov.marvinbot.core.domain.message.ReceivedMessage(chatId, userName, msgContent, cmd);
                dispatch(new MessageEvent(msg));
            }

            @EventHandler
            public void onContactRequest(com.samczsun.skype4j.events.contact.ContactRequestEvent ev) {
                try {
                    Contact contact = new Contact(ev.getRequest().getSender().getUsername());
                    LogManager.getLogger("core-logger").info("new contact request from " + contact.getName());

                    dispatch(new ContactRequestEvent(contact));
                } catch (ConnectionException e) {
                    e.printStackTrace();
                }
            }

            @EventHandler
            public void onChatJoinEvent(ChatJoinedEvent ev) {
                String chatId = ev.getChat().getIdentity();
                String initiatorName = ev.getInitiator().getUsername();
                dispatch(new BotJoinedChatEvent(chatId, initiatorName));
                LogManager.getLogger("core-logger").info("joined chat " + chatId);
            }

            @EventHandler
            public void onChatQuitEvent(ChatQuitEvent ev) {
                String chatId = ev.getChat().getIdentity();
                String initiatorName = ev.getInitiator().getUsername();
                dispatch(new BotLeftChatEvent(chatId, initiatorName));
                LogManager.getLogger("core-logger").info("left chat " + chatId);
            }
        });

        try {
            this.skype.subscribe();
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }
}
