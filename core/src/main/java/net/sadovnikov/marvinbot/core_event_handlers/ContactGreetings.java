package net.sadovnikov.marvinbot.core_event_handlers;

import com.google.inject.Inject;
import net.sadovnikov.marvinbot.core.service.contact.ContactManager;
import net.sadovnikov.marvinbot.core.domain.message.MessageToSend;
import net.sadovnikov.marvinbot.core.events.EventHandler;
import net.sadovnikov.marvinbot.core.events.event_types.ContactRequestEvent;
import net.sadovnikov.marvinbot.core.service.message.MessageSenderService;
import net.sadovnikov.marvinbot.core.service.message.MessageSenderException;
import org.apache.logging.log4j.Logger;
import ro.fortsoft.pf4j.Extension;

import java.util.Locale;
import java.util.ResourceBundle;

@Extension
public class ContactGreetings extends EventHandler<ContactRequestEvent> {

    ContactManager contactManager;
    MessageSenderService messageSenderService;
    ResourceBundle locData;
    Logger logger;

    @Inject
    public ContactGreetings(ContactManager contactManager, MessageSenderService messageSenderService, Locale locale, Logger logger) {
        this.contactManager = contactManager;
        this.messageSenderService = messageSenderService;
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
            messageSenderService.send(message);
        } catch (MessageSenderException e) {
            logger.catching(e);
        }
    }
}
