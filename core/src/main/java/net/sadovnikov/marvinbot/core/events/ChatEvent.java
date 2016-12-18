package net.sadovnikov.marvinbot.core.events;


import net.sadovnikov.marvinbot.core.domain.Channel;
import net.sadovnikov.marvinbot.core.service.chat.Chat;

public abstract class ChatEvent extends Event {

    protected String initiatorName;
    protected Channel channel;
    protected Chat chat;


    public String getChatId() {
        return this.chat.chatId();
    }
    public Chat chat() {
        return chat;
    }

    public String getInitiatorName() {
        return this.initiatorName;
    }

    public Channel channel() {
        return this.channel;
    }
}
