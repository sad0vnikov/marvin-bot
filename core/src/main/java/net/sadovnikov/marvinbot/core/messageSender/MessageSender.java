package net.sadovnikov.marvinbot.core.messageSender;

import net.sadovnikov.marvinbot.core.message.SentMessage;

public abstract class MessageSender {

    public abstract void sendMessage(SentMessage message);
}
