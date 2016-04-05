package net.sadovnikov.marvinbot.core.events.eventTypes;

import net.sadovnikov.marvinbot.core.events.Event;
import net.sadovnikov.marvinbot.core.message.ReceivedMessage;


/**
 * An event which dispatches up when a bot gets a new net.sadovnikov.marvinbot.core.message
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
