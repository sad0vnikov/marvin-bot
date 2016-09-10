package net.sadovnikov.marvinbot.core.service.contact;

import com.google.inject.Inject;
import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.exceptions.ConnectionException;
import net.sadovnikov.marvinbot.core.domain.Contact;
import net.sadovnikov.marvinbot.core.exceptions.SkypeApiException;
import net.sadovnikov.marvinbot.core.service.chat.AbstractChat;
import net.sadovnikov.marvinbot.core.service.chat.Chat;
import net.sadovnikov.marvinbot.core.service.chat.GroupChat;
import org.apache.logging.log4j.LogManager;

import java.util.Collection;
import java.util.HashSet;

public class Skype4jContactManager extends ContactManager {

    private Skype skype;

    @Inject
    public Skype4jContactManager(Skype skype) {
        this.skype = skype;
    }

    public void authorize(Contact contact) {
        try {
            skype.getContact(contact.getName()).authorize();

        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<AbstractChat> getAllChats() throws SkypeApiException {
        Collection<AbstractChat> chats = new HashSet<>();

        try {
            Collection<com.samczsun.skype4j.chat.Chat> skypeChats = skype.loadMoreChats(100);
            for (com.samczsun.skype4j.chat.Chat chat : skypeChats) {
                if (chat.getAllUsers().size() > 2) {
                    chats.add(new GroupChat(chat.getIdentity()));
                } else {
                    chats.add(new Chat(chat.getIdentity()));
                }
            }

            return chats;

        } catch (Exception e) {
            throw new SkypeApiException(e);
        }

    }
}
