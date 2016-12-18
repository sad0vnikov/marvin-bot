package net.sadovnikov.marvinbot.core.events.event_types;


import net.sadovnikov.marvinbot.core.domain.Channel;
import net.sadovnikov.marvinbot.core.events.ChatEvent;
import net.sadovnikov.marvinbot.core.service.chat.Chat;

public class BotJoinedChatEvent extends ChatEvent {


    public BotJoinedChatEvent(Chat chat, String initiatorUsername) {
        this.chat = chat;
        this.channel = chat.channel();
        this.initiatorName = initiatorUsername;
    }

    public BotJoinedChatEvent(Chat chat) {
        this.chat = chat;
    }

}
