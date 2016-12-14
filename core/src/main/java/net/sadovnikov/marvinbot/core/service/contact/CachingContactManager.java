package net.sadovnikov.marvinbot.core.service.contact;

import net.sadovnikov.marvinbot.core.domain.Contact;
import net.sadovnikov.marvinbot.core.exceptions.SkypeApiException;
import net.sadovnikov.marvinbot.core.service.chat.AbstractChat;
import net.sadovnikov.marvinbot.core.service.chat.Chat;

import java.util.Collection;

public abstract class CachingContactManager extends ContactManager {

    public abstract void setAsActive(Chat chat);

    public abstract void removeFromActive(Chat chat);
}
