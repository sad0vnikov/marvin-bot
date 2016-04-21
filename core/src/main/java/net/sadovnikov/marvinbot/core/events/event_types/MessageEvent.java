package net.sadovnikov.marvinbot.core.events.event_types;

import net.sadovnikov.marvinbot.core.events.Event;
import net.sadovnikov.marvinbot.core.events.EventFilter;
import net.sadovnikov.marvinbot.core.events.event_filters.CommandFilter;
import net.sadovnikov.marvinbot.core.message.ReceivedMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * An event which dispatches up when a bot gets a new net.sadovnikov.marvinbot.core.message
 */
public class MessageEvent extends Event {

    private ReceivedMessage message;

    public MessageEvent(ReceivedMessage message) {
        this.message = message;
        this.filters.add(new CommandFilter());
    }

    public ReceivedMessage getMessage() {
        return this.message;
    }
}
