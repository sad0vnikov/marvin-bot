package net.sadovnikov.marvinbot.core.service.contact;

import com.google.inject.Inject;
import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.exceptions.ConnectionException;
import net.sadovnikov.marvinbot.core.domain.Contact;

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
}
