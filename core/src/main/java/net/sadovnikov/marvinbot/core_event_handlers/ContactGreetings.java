package net.sadovnikov.marvinbot.core_event_handlers;

import com.google.inject.Inject;
import net.sadovnikov.marvinbot.core.contact.ContactManager;
import net.sadovnikov.marvinbot.core.events.EventHandler;
import net.sadovnikov.marvinbot.core.events.event_types.ContactRequestEvent;
import net.sadovnikov.marvinbot.core.message.SentMessage;
import net.sadovnikov.marvinbot.core.message_sender.MessageSender;
import ro.fortsoft.pf4j.Extension;

import java.util.Locale;
import java.util.ResourceBundle;

@Extension
public class ContactGreetings extends EventHandler<ContactRequestEvent> {

    ContactManager contactManager;
    MessageSender messageSender;
    ResourceBundle locData;

    @Inject
    public ContactGreetings(ContactManager contactManager, MessageSender messageSender, Locale locale) {
        this.contactManager = contactManager;
        this.messageSender  = messageSender;
        this.locData = ResourceBundle.getBundle("loc_data_main", locale);
    }

    @Override
    public void handle(ContactRequestEvent ev) {
        contactManager.authorize(ev.getContact());
        SentMessage message = new SentMessage();
        message.setRecepientId(ev.getContact().getName());
        message.setText(locData.getString("chatGreeting"));

        messageSender.sendMessage(message);
    }
}
