package injection;

import com.google.inject.AbstractModule;
import com.samczsun.skype4j.Skype;
import contact.ContactManager;
import contact.Skype4jContactManager;
import events.EventDispatcher;
import events.eventDispatchers.Skype4jEventDispatcher;
import messageSender.MessageSender;
import messageSender.Skype4jMessageSender;


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
