package net.sadovnikov.marvinbot.core.message_sender;

import net.sadovnikov.marvinbot.core.message.SentMessage;

public abstract class MessageSender {

    public abstract void sendMessage(SentMessage message);
}
