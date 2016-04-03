package messageSender;

import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.samczsun.skype4j.formatting.Text;
import message.Message;
import message.SentMessage;

public class Skype4jMessageSender extends MessageSender {

    Skype skype;

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