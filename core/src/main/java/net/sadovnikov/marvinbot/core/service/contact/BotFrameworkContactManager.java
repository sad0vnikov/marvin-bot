package net.sadovnikov.marvinbot.core.service.contact;

import com.google.inject.Inject;
import net.sadovnikov.marvinbot.core.db.repository.ActiveChatsRepository;
import net.sadovnikov.marvinbot.core.domain.Contact;
import net.sadovnikov.marvinbot.core.exceptions.SkypeApiException;
import net.sadovnikov.marvinbot.core.service.chat.AbstractChat;
import net.sadovnikov.marvinbot.core.service.chat.Chat;

import java.util.Collection;

public class BotFrameworkContactManager extends CachingContactManager {

    private ActiveChatsRepository activeChatsRepository;

    @Inject
    public BotFrameworkContactManager(ActiveChatsRepository activeChatsRepository) {
        this.activeChatsRepository = activeChatsRepository;
    }

    @Override
    public void authorize(Contact contact) {
        //we don't need to authorize contacts while using Bot framework api
    }

    @Override
    public Collection<AbstractChat> getAllChats() throws SkypeApiException {
        return activeChatsRepository.getAll();
    }

    @Override
    public void setAsActive(Chat chat) {
        activeChatsRepository.add(chat);
    }

    @Override
    public void removeFromActive(Chat chat) {
        activeChatsRepository.remove(chat);
    }
}
