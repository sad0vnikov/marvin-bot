package net.sadovnikov.marvinbot.core.service.message;

import com.google.inject.Inject;
import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.chat.messages.ChatMessage;
import com.samczsun.skype4j.exceptions.ChatNotFoundException;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.samczsun.skype4j.formatting.Text;
import net.sadovnikov.marvinbot.core.domain.message.Attachment;
import net.sadovnikov.marvinbot.core.domain.message.MessageToSend;
import net.sadovnikov.marvinbot.core.domain.message.SentMessage;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;

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
            Chat chat = skype.getOrLoadChat(message.chat().chatId());

            String messageId = null;

            if (message.text().length() > 0) {
                ChatMessage skype4jMessage = chat.sendMessage(toSend);
                messageId = skype4jMessage.getId();
            }

            SentMessage sentMessage = new SentMessage(messageId, message.chat(), message.text());

            for (Attachment attachment : message.attachments()) {
                if (attachment.isImage()) {
                    chat.sendImage(attachment.file());
                    continue;
                }
                chat.sendFile(attachment.file());
            }

            return sentMessage;

        } catch (ConnectionException | IOException e) {
            e.printStackTrace();
            throw new MessageSenderException(e);
        } catch (ChatNotFoundException e) {
            LogManager.getLogger("core-logger").error("net.sadovnikov.marvinbot.core.domain.message sender error: chat " + message.chat() + " not found");
            throw new MessageSenderException(e);
        }
    }
}