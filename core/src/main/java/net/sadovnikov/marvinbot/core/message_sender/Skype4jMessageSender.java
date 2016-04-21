package net.sadovnikov.marvinbot.core.message_sender;

import com.google.inject.Inject;
import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.samczsun.skype4j.formatting.Text;
import net.sadovnikov.marvinbot.core.message.ReceivedMessage;
import net.sadovnikov.marvinbot.core.message.SentMessage;

public class Skype4jMessageSender extends MessageSender {

    Skype skype;

    @Inject
    public Skype4jMessageSender(Skype skype) {
        this.skype = skype;
    }

    public void sendMessage(SentMessage message) {
        com.samczsun.skype4j.formatting.Message toSend = com.samczsun.skype4j.formatting.Message.create();
        toSend.with(Text.plain(message.getText()));

        try {
            skype.getChat(message.getRecepientId()).sendMessage(toSend);

        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }
}