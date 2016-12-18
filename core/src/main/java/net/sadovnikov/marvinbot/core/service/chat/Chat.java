package net.sadovnikov.marvinbot.core.service.chat;


import net.sadovnikov.marvinbot.core.domain.Channel;

public class Chat extends AbstractChat {

    protected String chatId;
    protected Channel channel;

    public Chat(Channel channel, String chatId) {
        this.chatId = chatId;
        this.channel = channel;
    }

    public String chatId() {
        return chatId;
    }

    public Channel channel() {
        return channel;
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
