package net.sadovnikov.marvinbot.core.events.event_types;


import net.sadovnikov.marvinbot.core.events.ChatEvent;

public class BotJoinedChatEvent extends ChatEvent {


    public BotJoinedChatEvent(String chatId) {
        this.chatId = chatId;
    }

}
