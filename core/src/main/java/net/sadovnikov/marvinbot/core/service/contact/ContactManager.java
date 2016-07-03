package net.sadovnikov.marvinbot.core.service.contact;


import net.sadovnikov.marvinbot.core.domain.Contact;

public abstract class ContactManager {

    public Contact getContactByName(String name) {
        return new Contact(name);
    }

    public abstract void authorize(Contact contact);
}