package net.sadovnikov.marvinbot.core.domain.message;

public class SentMessage extends Message {

    protected String id;
    protected String chatId;

    public SentMessage(String id, String chatId, String text) {
        this.id     = id;
        this.chatId = chatId;
        this.text   = text;
    }

    public String chatId() {
        return this.chatId;
    }

    public String id() {
        return id;
    }

    public boolean equals(SentMessage msg) {
        return msg.id().equals(id());
    }

}
