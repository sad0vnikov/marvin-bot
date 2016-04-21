package net.sadovnikov.marvinbot.core.message_sender;

import net.sadovnikov.marvinbot.core.message.ReceivedMessage;
import net.sadovnikov.marvinbot.core.message.SentMessage;

public abstract class MessageSender {

    public abstract void sendMessage(SentMessage message);

    public void reply(ReceivedMessage message, String text) {
        SentMessage outMessage = new SentMessage();
        outMessage.setRecepientId(message.getChatId());
        outMessage.setText(text);

        sendMessage(outMessage);
    }
}
