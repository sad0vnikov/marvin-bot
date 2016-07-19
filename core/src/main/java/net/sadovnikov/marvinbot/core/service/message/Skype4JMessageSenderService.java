package net.sadovnikov.marvinbot.core.service.message;

import com.google.inject.Inject;
import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.chat.messages.ChatMessage;
import com.samczsun.skype4j.exceptions.ChatNotFoundException;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.samczsun.skype4j.formatting.Text;
import net.sadovnikov.marvinbot.core.domain.message.MessageToSend;
import net.sadovnikov.marvinbot.core.domain.message.SentMessage;
import org.apache.logging.log4j.LogManager;

public class Skype4JMessageSenderService extends MessageSenderService {

    Skype skype;

    @Inject
    public Skype4JMessageSenderService(Skype skype) {
        this.skype = skype;
    }

    public SentMessage send(MessageToSend message) throws MessageSenderException {
        com.samczsun.skype4j.formatting.Message toSend = com.samczsun.skype4j.formatting.Message.create();
        toSend.with(Text.plain(message.text()));

        try {
            Chat chat = skype.getOrLoadChat(message.recepientId());
            ChatMessage skype4jMessage = chat.sendMessage(toSend);
            SentMessage sentMessage    = new SentMessage(skype4jMessage.getId(), message.recepientId(), message.text());

            return sentMessage;

        } catch (ConnectionException e) {
            e.printStackTrace();
            throw new MessageSenderException(e);
        } catch (ChatNotFoundException e) {
            LogManager.getLogger("core-logger").error("net.sadovnikov.marvinbot.core.domain.message sender error: chat " + message.recepientId() + " not found");
            throw new MessageSenderException(e);
        }
    }
}