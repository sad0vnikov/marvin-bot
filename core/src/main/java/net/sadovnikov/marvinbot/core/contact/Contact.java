package net.sadovnikov.marvinbot.core.contact;

public class Contact {
    private String name;
    private ContactManager manager;

    public Contact(ContactManager manager, String name) {
        this.name = name;
        this.manager = manager;
    }

    public String getName() {
        return this.name;
    }
}
