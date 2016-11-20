package net.sadovnikov.marvinbot.core.service.contact;

import net.sadovnikov.marvinbot.core.domain.Contact;
import net.sadovnikov.marvinbot.core.exceptions.SkypeApiException;
import net.sadovnikov.marvinbot.core.service.chat.AbstractChat;

import java.util.Collection;

public class BotFrameworkContactManager extends ContactManager {

    @Override
    public void authorize(Contact contact) {

    }

    @Override
    public Collection<AbstractChat> getAllChats() throws SkypeApiException {
        return null;
    }
}
