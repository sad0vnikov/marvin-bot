package events.eventHandlers.messages;

import events.EventHandler;
import events.eventTypes.MessageEvent;
import message.SentMessage;
import messageSender.MessageSender;

/**
 * This is a simple MessageHandler which just sends you your message back.
 */
public class EchoMessageHandler extends EventHandler<MessageEvent> {

    MessageSender sender;

    public EchoMessageHandler(MessageSender sender) {
        this.sender = sender;
    }

    public void handle(MessageEvent ev) {

        SentMessage message = new SentMessage();

        message.setRecepientId(ev.getMessage().getSenderId());
        message.setText(ev.getMessage().getText());
        sender.sendMessage(message);
    }
}
