package net.sadovnikov.marvinbot.core.events.event_types;

import net.sadovnikov.marvinbot.core.events.ChatEvent;
import net.sadovnikov.marvinbot.core.events.Event;

public class BotLeftChatEvent extends ChatEvent {

    public BotLeftChatEvent(String chatId, String initiatorUsername) {
        this.chatId = chatId;
        this.initiatorName = initiatorUsername;
    }
}
