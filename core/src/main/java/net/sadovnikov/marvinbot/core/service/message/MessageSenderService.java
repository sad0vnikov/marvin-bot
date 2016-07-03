package net.sadovnikov.marvinbot.core.service.message;

import com.samczsun.skype4j.chat.messages.SentMessage;
import net.sadovnikov.marvinbot.core.domain.message.MessageToSend;

public abstract class MessageSenderService {

    public abstract SentMessage send(MessageToSend msg);

}

