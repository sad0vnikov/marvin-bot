package net.sadovnikov.marvinbot.core.events.eventDispatchers;

import com.google.inject.Inject;
import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.chat.messages.ReceivedMessage;
import com.samczsun.skype4j.events.EventHandler;
import com.samczsun.skype4j.events.Listener;
import com.samczsun.skype4j.events.chat.message.MessageReceivedEvent;
import com.samczsun.skype4j.exceptions.ConnectionException;
import net.sadovnikov.marvinbot.core.contact.Contact;
import net.sadovnikov.marvinbot.core.events.EventDispatcher;
import net.sadovnikov.marvinbot.core.events.eventTypes.ContactRequestEvent;
import net.sadovnikov.marvinbot.core.events.eventTypes.MessageEvent;
import org.apache.logging.log4j.LogManager;

/**
 * An EventDispatcher implementation whilch is a wrapper for Skype4j event dispatcher.
 * Dispatches any kind of skype net.sadovnikov.marvinbot.core.events
 */
public class Skype4jEventDispatcher extends EventDispatcher {

    Skype skype;


    @Inject
    public Skype4jEventDispatcher(Skype skype) {
        this.skype = skype;
    }


    public void run() {

        this.skype.getEventDispatcher().registerListener(new Listener() {

            @EventHandler
            public void onMessage(MessageReceivedEvent ev) {
                ReceivedMessage skype4jMessage = ev.getMessage();
                String msgContent = skype4jMessage.getContent().asPlaintext();
                skype4jMessage.getChat().getIdentity();
                String chatId = skype4jMessage.getChat().getIdentity();

                String userName = skype4jMessage.getSender().getUsername();

                net.sadovnikov.marvinbot.core.message.ReceivedMessage msg = new net.sadovnikov.marvinbot.core.message.ReceivedMessage(chatId, userName, msgContent);
                dispatch(new MessageEvent(msg));
            }

            @EventHandler
            public void onContactRequest(com.samczsun.skype4j.events.contact.ContactRequestEvent ev) {
                try {
                    Contact contact = new Contact(ev.getRequest().getSender().getUsername());
                    dispatch(new ContactRequestEvent(contact));
                } catch (ConnectionException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            this.skype.subscribe();
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }
}
