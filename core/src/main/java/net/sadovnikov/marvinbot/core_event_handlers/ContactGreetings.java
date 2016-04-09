package net.sadovnikov.marvinbot.core_event_handlers;

import com.google.inject.Inject;
import net.sadovnikov.marvinbot.core.contact.ContactManager;
import net.sadovnikov.marvinbot.core.events.eventHandlers.ContactRequestEventHandler;
import net.sadovnikov.marvinbot.core.events.eventTypes.ContactRequestEvent;
import net.sadovnikov.marvinbot.core.message.SentMessage;
import net.sadovnikov.marvinbot.core.messageSender.MessageSender;
import ro.fortsoft.pf4j.Extension;

@Extension
public class ContactGreetings extends ContactRequestEventHandler {

    ContactManager contactManager;
    MessageSender messageSender;

    @Inject
    public ContactGreetings(ContactManager contactManager, MessageSender messageSender) {
        this.contactManager = contactManager;
        this.messageSender  = messageSender;
    }

    @Override
    public void handle(ContactRequestEvent ev) {
        contactManager.authorize(ev.getContact());
        SentMessage message = new SentMessage();
        message.setRecepientId(ev.getContact().getName());
        message.setText("Hello! My name is Marvin!");

        messageSender.sendMessage(message);
    }
}
