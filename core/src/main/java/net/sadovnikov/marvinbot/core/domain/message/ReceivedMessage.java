package net.sadovnikov.marvinbot.core.domain.message;

public class ReceivedMessage extends Message{

    private String id;
    private String chatId;
    private String userName;

    public ReceivedMessage(String chatId, String userName, String text) {
        this.text     = text;
        this.chatId   = chatId;
        this.userName = userName;
    }

    public String id() {
        return this.id;
    }

    public String chatId() {
        return this.chatId;
    }

    public String senderUsername() {
        return this.userName;
    }

    public boolean equals(ReceivedMessage message) {
        return message.id().equals(id);
    }
}
