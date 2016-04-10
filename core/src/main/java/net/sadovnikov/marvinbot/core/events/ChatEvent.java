package net.sadovnikov.marvinbot.core.events;


public abstract class ChatEvent extends Event {

    protected String chatId;


    public String getChatId() {
        return this.chatId;
    }
}
