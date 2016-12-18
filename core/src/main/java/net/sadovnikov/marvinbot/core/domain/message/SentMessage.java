package net.sadovnikov.marvinbot.core.domain.message;

import net.sadovnikov.marvinbot.core.service.chat.AbstractChat;
import net.sadovnikov.marvinbot.core.service.chat.Chat;

public class SentMessage extends Message {

    protected String id;
    protected AbstractChat chat;

    public SentMessage(String id, AbstractChat chat, String text) {
        this.id     = id;
        this.chat   = chat;
        this.text   = text;
    }

    public String chatId() {
        return this.chat.chatId();
    }

    public AbstractChat chat() {
        return chat;
    }

    public String id() {
        return id;
    }

    public boolean equals(SentMessage msg) {
        return msg.id().equals(id());
    }

}
