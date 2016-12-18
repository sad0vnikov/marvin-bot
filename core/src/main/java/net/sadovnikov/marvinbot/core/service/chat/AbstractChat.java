package net.sadovnikov.marvinbot.core.service.chat;

import net.sadovnikov.marvinbot.core.domain.Channel;

public abstract class AbstractChat {

    public boolean isGroupChat() {
        return false;
    }

    public abstract String chatId();

    public abstract Channel channel();
}
