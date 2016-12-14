package net.sadovnikov.marvinbot.core.service.chat;


public class Chat extends AbstractChat {

    protected String chatId;

    public Chat(String chatId) {
        this.chatId = chatId;
    }

    public String chatId() {
        return chatId;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Chat)) {
            return false;
        }

        Chat chat = (Chat) obj;
        return chatId().equals(chat.chatId());
    }

    @Override
    public int hashCode() {
        return chatId != null ? chatId.hashCode() : 0;
    }
}
