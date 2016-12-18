package net.sadovnikov.marvinbot.core.domain.message;

import net.sadovnikov.marvinbot.core.domain.Channel;
import net.sadovnikov.marvinbot.core.domain.Command;
import net.sadovnikov.marvinbot.core.service.chat.Chat;

public class ReceivedMessage extends Message{

    private String id;
    private Chat chat;
    private String userName;

    private Command command;
    private boolean isCommand = false;

    public ReceivedMessage(Chat chat, String userName, String text, Command command) {
        this.text     = text;
        this.chat  = chat;
        this.userName = userName;
        if (command != null) {
            this.command = command;
            this.isCommand = true;
        }
    }

    public ReceivedMessage(Chat chat, String userName, String text) {
        this(chat, userName, text, null);
    }

    public String id() {
        return this.id;
    }

    public String chatId() {
        return this.chat.chatId();
    }

    public String senderUsername() {
        return this.userName;
    }

    public boolean isCommand() {
        return this.isCommand;
    }

    public Command command() {
        return this.command;
    }

    public Channel channel() {
        return this.chat.channel();
    }

    public Chat chat() {
        return chat;
    }

    public boolean equals(ReceivedMessage message) {
        return message.id().equals(id);
    }
}
