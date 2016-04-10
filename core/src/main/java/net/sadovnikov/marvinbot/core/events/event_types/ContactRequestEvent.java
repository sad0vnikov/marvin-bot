package net.sadovnikov.marvinbot.core.events.event_types;

import net.sadovnikov.marvinbot.core.contact.Contact;
import net.sadovnikov.marvinbot.core.events.Event;

/**
 * An event which dispatches up when a bot gets a new net.sadovnikov.marvinbot.core.contact request
 */
public class ContactRequestEvent extends Event {

    private Contact contact;

    public ContactRequestEvent(Contact contact) {
        this.contact = contact;
    }

    public Contact getContact() {
        return this.contact;
    }
}
