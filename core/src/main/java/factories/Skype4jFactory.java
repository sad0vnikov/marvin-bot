package factories;

import com.google.inject.AbstractModule;
import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.SkypeBuilder;
import com.samczsun.skype4j.Visibility;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.samczsun.skype4j.exceptions.InvalidCredentialsException;
import com.samczsun.skype4j.exceptions.NotParticipatingException;
import contact.ContactManager;
import contact.Skype4jContactManager;
import events.EventDispatcher;
import events.eventDispatchers.Skype4jEventDispatcher;
import messageSender.MessageSender;
import messageSender.Skype4jMessageSender;

public class Skype4jFactory extends AbstractModule {

    Skype skype;

    public Skype4jFactory(String login, String password) {
        this.skype = new SkypeBuilder(login, password).withAllResources().build();
        try {
            skype.login();
            skype.subscribe();
            skype.setVisibility(Visibility.ONLINE);
        } catch (InvalidCredentialsException | NotParticipatingException | ConnectionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void configure() {
        bind(MessageSender.class).to(Skype4jMessageSender.class);
        bind(EventDispatcher.class).to(Skype4jEventDispatcher.class);
        bind(ContactManager.class).to(Skype4jContactManager.class);
        bind(Skype.class).toInstance(this.skype);
    }
}
