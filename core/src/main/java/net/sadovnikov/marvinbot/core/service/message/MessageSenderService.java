package net.sadovnikov.marvinbot.core.service.message;

import net.sadovnikov.marvinbot.core.domain.message.MessageToSend;
import net.sadovnikov.marvinbot.core.domain.message.ReceivedMessage;
import net.sadovnikov.marvinbot.core.domain.message.SentMessage;

public abstract class MessageSenderService {

    public abstract SentMessage send(MessageToSend message) throws MessageSenderException;

    public SentMessage reply(ReceivedMessage message, String text) throws MessageSenderException {
        String recepientId = message.chatId();
        MessageToSend outMessage = new MessageToSend(text, recepientId);

        return send(outMessage);
    }
}
