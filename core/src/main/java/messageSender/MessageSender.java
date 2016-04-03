package messageSender;

import message.Message;
import message.SentMessage;

public abstract class MessageSender {

    public abstract void sendMessage(SentMessage message);
}
