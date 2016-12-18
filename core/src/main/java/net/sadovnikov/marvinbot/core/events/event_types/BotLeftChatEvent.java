package net.sadovnikov.marvinbot.core.events.event_types;

import net.sadovnikov.marvinbot.core.domain.Channel;
import net.sadovnikov.marvinbot.core.events.ChatEvent;
import net.sadovnikov.marvinbot.core.events.Event;
import net.sadovnikov.marvinbot.core.service.chat.Chat;

public class BotLeftChatEvent extends ChatEvent {

    public BotLeftChatEvent(Chat chat, String initiatorUsername) {
        this.chat = chat;
        this.channel = chat.channel();
        this.initiatorName = initiatorUsername;
    }

    public BotLeftChatEvent(Chat chat) {
        this.chat = chat;
    }

}
