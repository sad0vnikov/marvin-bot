package net.sadovnikov.marvinbot.core.domain.message;

import net.sadovnikov.marvinbot.core.domain.Command;

public class ReceivedMessage extends Message{

    private String id;
    private String chatId;
    private String userName;

    private Command command;
    private boolean isCommand = false;

    public ReceivedMessage(String chatId, String userName, String text, Command command) {
        this.text     = text;
        this.chatId   = chatId;
        this.userName = userName;
        if (command != null) {
            this.command = command;
            this.isCommand = true;
        }
    }

    public ReceivedMessage(String chatId, String userName, String text) {
        this(chatId, userName, text, null);
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

    public boolean isCommand() {
        return this.isCommand;
    }

    public Command command() {
        return this.command;
    }

    public boolean equals(ReceivedMessage message) {
        return message.id().equals(id);
    }
}
