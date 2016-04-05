package factories;


import contact.ContactManager;
import events.EventDispatcher;
import messageSender.MessageSender;

public abstract class SkypeFactory {

    public abstract MessageSender getMessageSender();

    public abstract EventDispatcher getEventDispatcher();

    public abstract ContactManager getContactManager();

}
