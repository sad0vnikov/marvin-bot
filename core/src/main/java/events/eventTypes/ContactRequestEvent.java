package events.eventTypes;

import contact.Contact;
import events.Event;


public class ContactRequestEvent extends Event {

    private Contact contact;

    public ContactRequestEvent(Contact contact) {
        this.contact = contact;
    }

    public Contact getContact() {
        return this.contact;
    }
}
