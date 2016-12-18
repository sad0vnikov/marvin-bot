package net.sadovnikov.marvinbot.core.service.chat;

import net.sadovnikov.marvinbot.core.domain.Channel;

public class GroupChat extends Chat {

    public GroupChat(Channel channel, String chatId) {
        super(channel, chatId);
    }

    @Override
    public boolean isGroupChat() {
        return true;
    }
}
