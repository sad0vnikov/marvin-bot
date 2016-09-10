package net.sadovnikov.marvinbot.core.service.chat;

public abstract class AbstractChat {

    public boolean isGroupChat() {
        return false;
    }

    public abstract String chatId();
}
