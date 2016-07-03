package net.sadovnikov.marvinbot.core_event_handlers;

import com.google.inject.Inject;
import net.sadovnikov.marvinbot.core.service.contact.ContactManager;
import net.sadovnikov.marvinbot.core.domain.message.MessageToSend;
import net.sadovnikov.marvinbot.core.events.EventHandler;
import net.sadovnikov.marvinbot.core.events.event_types.ContactRequestEvent;
import net.sadovnikov.marvinbot.core.service.message_sender.MessageSender;
import net.sadovnikov.marvinbot.core.service.message_sender.MessageSenderException;
import org.apache.logging.log4j.Logger;
import ro.fortsoft.pf4j.Extension;

import java.util.Locale;
import java.util.ResourceBundle;

@Extension
public class ContactGreetings extends EventHandler<ContactRequestEvent> {

    ContactManager contactManager;
    MessageSender messageSender;
    ResourceBundle locData;
    Logger logger;

    @Inject
    public ContactGreetings(ContactManager contactManager, MessageSender messageSender, Locale locale, Logger logger) {
        this.contactManager = contactManager;
        this.messageSender  = messageSender;
        this.locData = ResourceBundle.getBundle("loc_data_main", locale);
        this.logger = logger;
    }

    @Override
    public void handle(ContactRequestEvent ev) {
        contactManager.authorize(ev.getContact());
        String msgText = locData.getString("chatGreeting");
        String chatId  = ev.getContact().getName();
        MessageToSend message = new MessageToSend(msgText, chatId);

        try {
            messageSender.sendMessage(message);
        } catch (MessageSenderException e) {
            logger.catching(e);
        }
    }
}
