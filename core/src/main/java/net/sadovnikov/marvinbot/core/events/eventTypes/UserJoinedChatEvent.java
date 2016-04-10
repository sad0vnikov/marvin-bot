package net.sadovnikov.marvinbot.core.events.eventTypes;


import net.sadovnikov.marvinbot.core.events.ChatEvent;

public class UserJoinedChatEvent extends ChatEvent {

    protected String userName;

    public UserJoinedChatEvent(String chatId, String userName) {
        this.chatId = chatId;
        this.userName = userName;
    }

}
