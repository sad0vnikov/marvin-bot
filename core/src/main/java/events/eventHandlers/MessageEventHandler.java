package events.eventHandlers;

import com.sun.istack.internal.NotNull;
import events.EventHandler;
import events.eventTypes.MessageEvent;
import messageSender.MessageSender;
import ro.fortsoft.pf4j.ExtensionPoint;



abstract public class MessageEventHandler extends EventHandler<MessageEvent> {

    @NotNull
    protected MessageSender messageSender;

    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public MessageSender getMessageSender() {
        return this.messageSender;
    }
}
