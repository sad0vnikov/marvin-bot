package events.eventListeners;

import events.EventListener;
import events.eventHandlers.messages.EchoMessageHandler;
import events.eventTypes.MessageEvent;
import messageSender.MessageSender;

public class MessageEventListener extends EventListener<MessageEvent> {

    MessageSender sender;

    public MessageEventListener(MessageSender sender) {
        this.sender = sender;
    }

    public void send(MessageEvent ev) {
        String msgText = ev.getMessage().getText();
        new EchoMessageHandler(sender).handle(ev);
    }
}
