package net.sadovnikov.marvinbot.core.service.chat;

public class GroupChat extends Chat {

    public GroupChat(String chatId) {
        super(chatId);
    }

    @Override
    public boolean isGroupChat() {
        return true;
    }
}
