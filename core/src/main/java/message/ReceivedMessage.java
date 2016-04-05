package message;

import contact.Contact;

public class ReceivedMessage extends Message{

    private String chatId;
    private String userName;

    public ReceivedMessage(String chatId, String userName, String text) {
        this.text     = text;
        this.chatId   = chatId;
        this.userName = userName;
    }

    public String getChatId() {
        return this.chatId;
    }

    public String getSenderUsername() {
        return this.userName;
    }
}
