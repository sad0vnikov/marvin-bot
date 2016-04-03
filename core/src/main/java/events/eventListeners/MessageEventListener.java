package events.eventListeners;

import events.EventListener;
import events.eventHandlers.messages.EchoMessageHandler;
import events.eventTypes.MessageEvent;
import messageSender.MessageSender;
import org.apache.logging.log4j.LogManager;

public class MessageEventListener extends EventListener<MessageEvent> {

    MessageSender sender;

    public MessageEventListener(MessageSender sender) {
        this.sender = sender;
    }

    public void send(MessageEvent ev) {
        String msgText = ev.getMessage().getText();
        String usrName = ev.getMessage().getUser().getName();

        LogManager.getLogger("core-logger").debug("[received message message]" + usrName + ": " + msgText);
        new EchoMessageHandler(sender).handle(ev);
    }
}
