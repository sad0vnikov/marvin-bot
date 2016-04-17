package net.sadovnikov.marvinbot.core.events;


public abstract class ChatEvent extends Event {

    protected String chatId;
    protected String initiatorName;


    public String getChatId() {
        return this.chatId;
    }

    public String getInitiatorName() {
        return this.initiatorName;
    }
}
