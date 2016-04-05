package net.sadovnikov.marvinbot.core.injection;

import com.google.inject.AbstractModule;
import com.samczsun.skype4j.Skype;
import net.sadovnikov.marvinbot.core.contact.ContactManager;
import net.sadovnikov.marvinbot.core.contact.Skype4jContactManager;
import net.sadovnikov.marvinbot.core.events.EventDispatcher;
import net.sadovnikov.marvinbot.core.events.eventDispatchers.Skype4jEventDispatcher;
import net.sadovnikov.marvinbot.core.messageSender.MessageSender;
import net.sadovnikov.marvinbot.core.messageSender.Skype4jMessageSender;


/**
 * Binds core bot components to their implementations which use Skype4j lib
 */
public class Skype4jInjector extends AbstractModule {

    Skype skype;

    public Skype4jInjector(Skype skype) {
        this.skype = skype;
    }

    @Override
    public void configure() {
        bind(MessageSender.class).to(Skype4jMessageSender.class);
        bind(EventDispatcher.class).to(Skype4jEventDispatcher.class);
        bind(ContactManager.class).to(Skype4jContactManager.class);
        bind(Skype.class).toInstance(this.skype);

    }
}
