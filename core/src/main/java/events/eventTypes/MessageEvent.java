package events.eventTypes;

import events.Event;
import message.ReceivedMessage;


/**
 * An event which dispatches up when a bot gets a new message
 */
public class MessageEvent extends Event {

    private ReceivedMessage message;

    public MessageEvent(ReceivedMessage message) {
        this.message = message;
    }

    public ReceivedMessage getMessage() {
        return this.message;
    }
}
