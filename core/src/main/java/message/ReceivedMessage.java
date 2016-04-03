package message;

import user.User;

public class ReceivedMessage extends Message{

    private String chatId;
    private User user;

    public ReceivedMessage(String chatId, User user, String text) {
        this.text   = text;
        this.chatId = chatId;
        this.user   = user;
    }

    public String getChatId() {
        return this.chatId;
    }
}
