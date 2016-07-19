package net.sadovnikov.marvinbot.core.injection;

import com.google.inject.AbstractModule;
import com.samczsun.skype4j.Skype;
import net.sadovnikov.marvinbot.core.service.contact.ContactManager;
import net.sadovnikov.marvinbot.core.service.contact.Skype4jContactManager;
import net.sadovnikov.marvinbot.core.events.EventDispatcher;
import net.sadovnikov.marvinbot.core.events.event_dispatchers.Skype4jEventDispatcher;
import net.sadovnikov.marvinbot.core.service.message.MessageSenderService;
import net.sadovnikov.marvinbot.core.service.message.Skype4JMessageSenderService;
import net.sadovnikov.marvinbot.core.service.permissions.PermissionChecker;
import net.sadovnikov.marvinbot.core.service.permissions.Skype4jPermissionChecker;


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
        bind(MessageSenderService.class).to(Skype4JMessageSenderService.class);
        bind(EventDispatcher.class).to(Skype4jEventDispatcher.class);
        bind(ContactManager.class).to(Skype4jContactManager.class);
        bind(PermissionChecker.class).to(Skype4jPermissionChecker.class);
        bind(Skype.class).toInstance(this.skype);
    }
}
