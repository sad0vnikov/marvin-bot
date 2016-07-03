package net.sadovnikov.marvinbot.core.service.message_sender;

import net.sadovnikov.marvinbot.core.domain.message.MessageToSend;
import net.sadovnikov.marvinbot.core.domain.message.ReceivedMessage;
import net.sadovnikov.marvinbot.core.domain.message.SentMessage;

public abstract class MessageSender {

    public abstract SentMessage sendMessage(MessageToSend message) throws MessageSenderException;

    public SentMessage reply(ReceivedMessage message, String text) throws MessageSenderException {
        String recepientId = message.chatId();
        MessageToSend outMessage = new MessageToSend(text, recepientId);

        return sendMessage(outMessage);
    }
}
