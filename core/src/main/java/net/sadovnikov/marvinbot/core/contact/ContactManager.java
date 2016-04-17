package net.sadovnikov.marvinbot.core.contact;


public abstract class ContactManager {

    public Contact getContactByName(String name) {
        return new Contact(name);
    }

    public abstract void authorize(Contact contact);
}