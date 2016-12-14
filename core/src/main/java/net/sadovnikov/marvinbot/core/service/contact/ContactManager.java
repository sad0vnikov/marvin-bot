package net.sadovnikov.marvinbot.core.service.contact;


import net.sadovnikov.marvinbot.core.domain.Contact;
import net.sadovnikov.marvinbot.core.exceptions.SkypeApiException;
import net.sadovnikov.marvinbot.core.service.chat.AbstractChat;

import java.util.Collection;
import java.util.stream.Collectors;

public abstract class ContactManager {

    public Contact getContactByName(String name) {
        return new Contact(name);
    }

    public abstract void authorize(Contact contact);

    public abstract Collection<AbstractChat> getAllChats() throws SkypeApiException;

    public Collection<AbstractChat> getGroupChats() throws SkypeApiException {
        return getAllChats().stream()
                .filter(c -> c.isGroupChat()).collect(Collectors.toList());
    }
}