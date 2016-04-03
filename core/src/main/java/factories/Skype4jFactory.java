package factories;

import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.SkypeBuilder;
import com.samczsun.skype4j.Visibility;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.samczsun.skype4j.exceptions.InvalidCredentialsException;
import com.samczsun.skype4j.exceptions.NotParticipatingException;
import events.EventDispatcher;
import events.eventDispatchers.Skype4jEventDispatcher;
import messageSender.MessageSender;
import messageSender.Skype4jMessageSender;

public class Skype4jFactory extends SkypeFactory {

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

    public MessageSender getMessageSender() {
        return new Skype4jMessageSender(this.skype);
    }

    public EventDispatcher getEventDispatcher() {
        return new Skype4jEventDispatcher(this.skype);
    }
}
