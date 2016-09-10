package net.sadovnikov.marvinbot.core.service.chat;


public class Chat extends AbstractChat {

    protected String chatId;

    public Chat(String chatId) {
        this.chatId = chatId;
    }

    public String chatId() {
        return chatId;
    }
}
